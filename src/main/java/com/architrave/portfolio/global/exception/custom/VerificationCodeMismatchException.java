package com.architrave.portfolio.global.exception.custom;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class VerificationCodeMismatchException extends AuthenticationException {
    public VerificationCodeMismatchException(String msg) {
        super(msg);
    }
    public VerificationCodeMismatchException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
