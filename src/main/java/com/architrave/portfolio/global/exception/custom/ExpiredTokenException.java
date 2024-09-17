package com.architrave.portfolio.global.exception.custom;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class ExpiredTokenException extends AuthenticationException {
    public ExpiredTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ExpiredTokenException(String msg) {
        super(msg);
    }
}
