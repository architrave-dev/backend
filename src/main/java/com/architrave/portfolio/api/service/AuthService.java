package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.repository.MemberRepository;
import com.architrave.portfolio.global.aop.Trace;
import com.architrave.portfolio.global.exception.custom.ExpiredTokenException;
import com.architrave.portfolio.global.exception.custom.InvalidTokenException;
import com.architrave.portfolio.infra.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Trace
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

    public String getAccessToken(Member member){
        String jwtToken = jwtService.createJwt(member);
        return "Bearer " + jwtToken;
    }
    public String getRefreshToken(Member member){
        return jwtService.createRefreshToken(member);
    }

    public String refreshToken(String refreshToken){
        if(!jwtService.isExpired(refreshToken)){
            throw new ExpiredTokenException("refresh token expired");
        }
        log.info("refresh 남은 시간: " + jwtService.getLeftTime(refreshToken));
        String email = jwtService.extractEmailFromToken(refreshToken);
        if (email == null) {
            throw new InvalidTokenException("Invalid refresh token");
        }
        Member member = loadUserByUsername(email);
        String accessToken = jwtService.createJwt(member);
        return "Bearer " + accessToken;
    }
}
