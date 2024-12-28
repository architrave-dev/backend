package com.architrave.portfolio.domain.model.enumType;

public enum ErrorCode {
    IDF("Invalid Dto Field"),
    RVN("Required Value Null"),
    NFR("Not Found Result"),
    NAU("Not Authorized User"),
    AEV("Already Exist Value"),
    ATX("Access Token Expired"),
    RTX("Refresh Token Expired"),

    DUK("Duplicated Unique Key"),

    DBE("Database Error"),
    UME("UnManaged Error");;


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
