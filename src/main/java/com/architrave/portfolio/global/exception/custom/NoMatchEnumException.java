package com.architrave.portfolio.global.exception.custom;

import lombok.Getter;

@Getter
public class NoMatchEnumException extends IllegalArgumentException {
    public NoMatchEnumException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NoMatchEnumException(String msg) {
        super(msg);
    }
}
