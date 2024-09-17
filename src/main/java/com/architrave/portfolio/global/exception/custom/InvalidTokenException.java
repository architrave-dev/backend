package com.architrave.portfolio.global.exception.custom;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidTokenException(String msg) {
        super(msg);
    }
}
