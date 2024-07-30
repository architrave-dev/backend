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
import org.springframework.security.crypto.password.PasswordEncoder;
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
        return loadUserByUsername(email);
    }

    @Override
    public Member loadUserByUsername(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String login(String email){
        Member member = loadUserByUsername(email);

        String jwtToken = jwtService.createJwt(member);
        log.info("jwtToken: " + jwtToken);
        return "Bearer " + jwtToken;
    }

}
