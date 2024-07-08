package com.architrave.portfolio.api.dto.auth.request;

import lombok.Getter;

@Getter
public class LoginReq {
    private String email;
    private String password;
}
