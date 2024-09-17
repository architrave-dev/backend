package com.architrave.portfolio.infra.security;

import com.architrave.portfolio.api.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String email;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            log.info("there is no auth token");
            filterChain.doFilter(request, response);
            return;
        }
        log.info("we should check token in here");
        String jwtToken = authHeader.substring(7);

        try{
            email = jwtService.extractEmailFromToken(jwtToken); //expired 판별이 여기서 난다.
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                //DB에서 email로 찾기
                UserDetails userDetails = authService.loadUserByUsername(email);
                //expire 여부 확인
                if(!jwtService.isExpired(jwtToken)){
                    log.info("남은 시간: " + jwtService.getLeftTime(jwtToken));
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }catch (ExpiredJwtException e){
            request.setAttribute("exception", "ExpiredJwtException");
        }
        filterChain.doFilter(request, response);
    }
}
