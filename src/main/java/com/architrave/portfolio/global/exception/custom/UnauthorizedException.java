package com.architrave.portfolio.global.exception.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class UnauthorizedException extends AuthenticationException {
    public UnauthorizedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnauthorizedException(String msg) {
        super(msg);
    }
}
