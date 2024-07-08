package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.repository.MemberRepository.MemberRepository;
import com.architrave.portfolio.infra.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
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

    @Override
    public Member loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email).orElse(null);
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
