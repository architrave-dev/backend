package com.architrave.portfolio.api.dto.auth.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMemberReq{
    private String email;
    private String password;
    private String username;
}