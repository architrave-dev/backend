package com.architrave.portfolio.domain.model.enumType;

public enum ErrorCode {
    IVA("Invalid AUI"),
    RVN("Required Value Null");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return name();
    }
}
