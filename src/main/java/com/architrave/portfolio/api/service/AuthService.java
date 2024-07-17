package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.repository.MemberRepository;
import com.architrave.portfolio.infra.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    public Member getMemberFromContext(){
        String email = ((Member) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getEmail();
        if(email == null){
            // security에서 처리하니까 괜춘할듯...? 아닌가...? 그래도 해줘야하나?
        }
        return loadUserByUsername(email);
    }

    @Override
    public Member loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){
            throw new UsernameNotFoundException("User not found");
        }
        return member;
    }

    public String login(Member member, String password){
        if(member == null){
            throw new UsernameNotFoundException("User not found");
        }
        if(!member.getPassword().equals(password)){
            throw new BadCredentialsException("wrong password");
        }
        String jwtToken = jwtService.createJwt(member);
        log.info("jwtToken: " + jwtToken);
        return "Bearer " + jwtToken;
    }

}
