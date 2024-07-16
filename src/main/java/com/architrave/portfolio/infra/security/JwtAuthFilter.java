package com.architrave.portfolio.infra.security;

import com.architrave.portfolio.api.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authentication");
        String email;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            log.info("there is no auth token");
            filterChain.doFilter(request, response);
            return;
        }
        log.info("we should check token in here");
        String jwtToken = authHeader.substring(7);

        email = jwtService.extractEmailFromToken(jwtToken);

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //DB에서 email로 찾기
            UserDetails userDetails = authService.loadUserByUsername(email);

            //expire 여부 확인
            if(!jwtService.isExpired(jwtToken)){
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
        filterChain.doFilter(request, response);
    }
}
