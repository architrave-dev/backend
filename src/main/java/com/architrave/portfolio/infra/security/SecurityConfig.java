package com.architrave.portfolio.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((auth) -> auth.disable());

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/v1/auth/**", "/error").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/project/").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/landing_box/**").permitAll()
                .requestMatchers("/api/v1/project-element/**").permitAll()
                .requestMatchers("/api/v1/work/**").permitAll()
                .requestMatchers("/api/v1/project/**").permitAll()                     //해결 못하겠음... 일단 넘어가자
                .requestMatchers(HttpMethod.PUT,"/api/v1/landing_box/**").permitAll()  //해결 못하겠음... 일단 넘어가자
//                .requestMatchers(HttpMethod.PUT, "/api/v1/landing_box/**").hasRole("USER")
//                .requestMatchers("/api/v1/**").hasRole("USER")
                .anyRequest().authenticated()
        );

        http.formLogin((auth) -> auth.disable());
        http.logout((auth) -> auth.disable());
        http.httpBasic((auth) -> auth.disable());

        // authToken을 SecurityContext에 넣었을 뿐이지,
        // 해당 유저가 해당 권한이 있는지 확인하진 않았다?
        // FilterSecurityInterceptor가 확인한데 AuthorizationFilter 도 함께 봐보자
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        //세션 세팅
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
