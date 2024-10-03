package com.architrave.portfolio.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint; // 추가된 필드
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((auth) -> auth.disable());

        http.cors((auth) -> auth.configurationSource(corsConfigurationSource));

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(SWAGGER_WHITELIST).permitAll()                                //swagger
                .requestMatchers("/api/v1/auth/**", "/error").permitAll()
                .requestMatchers(HttpMethod.GET, CLIENT_WHITELIST).permitAll()
                .requestMatchers(HttpMethod.POST, USER_WHITELIST).hasRole("USER")
                .requestMatchers(HttpMethod.PUT, USER_WHITELIST).hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, USER_WHITELIST).hasRole("USER")
                .requestMatchers("/api/v1/**").hasRole("USER")
                .anyRequest().authenticated()
        );

        http.formLogin((auth) -> auth.disable());
        http.logout((auth) -> auth.disable());
        http.httpBasic((auth) -> auth.disable());
        
        http.authenticationProvider(authenticationProvider);

        // authToken을 SecurityContext에 넣었을 뿐이지,
        // 해당 유저가 해당 권한이 있는지 확인하진 않았다?
        // FilterSecurityInterceptor가 확인한데 AuthorizationFilter 도 함께 봐보자
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // 예외 처리 설정 추가
        http.exceptionHandling((ex) -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        //세션 세팅
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private static final String[] CLIENT_WHITELIST = {
            "/api/v1/member",
            "/api/v1/landing-box/**",
            "/api/v1/project",
            "/api/v1/project/**",
            "/api/v1/project-element/**",
            "/api/v1/work",
            "/api/v1/career",
            "/api/v1/member-info"
    };

    private static final String[] USER_WHITELIST = {
            "/api/v1/landing-box/**",
            "/api/v1/project",
            "/api/v1/project/**",
            "/api/v1/project-element/**",
            "/api/v1/work",
            "/api/v1/career",
            "/api/v1/member-info"
    };
}
