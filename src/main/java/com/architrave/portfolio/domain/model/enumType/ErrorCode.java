package com.architrave.portfolio.domain.model.enumType;

public enum ErrorCode {
    IDF("Invalid Dto Field"),
    RVN("Required Value Null"),
    NFR("Not Found Result"),
    NAU("Not Authorized User"),
    AEV("Already Exist Value");


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
