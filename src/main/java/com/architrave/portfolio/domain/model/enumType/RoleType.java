package com.architrave.portfolio.domain.model.enumType;

public enum RoleType {
    USER, ADMIN;

    public String getRoleName() {
        return "ROLE_" + name();
    }
}
