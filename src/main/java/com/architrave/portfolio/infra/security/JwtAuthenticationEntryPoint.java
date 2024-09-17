package com.architrave.portfolio.infra.security;

import com.architrave.portfolio.api.dto.ErrorDto;
import com.architrave.portfolio.domain.model.enumType.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException
    ) throws IOException {
        String exception = (String) request.getAttribute("exception");

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorDto errorDto;

        if (exception != null) {
            switch (exception) {
                case "ExpiredJwtException":
                    errorDto = new ErrorDto(ErrorCode.ATX, "Access Token expired.");
                    break;
                default:
                    errorDto = new ErrorDto(ErrorCode.UME, "security에서 핸들링되지 않은 에러가 발생했습니다.");
            }
        }else{
            errorDto = new ErrorDto(ErrorCode.UME, "핸들링되지 않은 에러가 발생했습니다.");
        }

        String json = objectMapper.writeValueAsString(errorDto);
        response.getWriter().write(json);
    }
}
