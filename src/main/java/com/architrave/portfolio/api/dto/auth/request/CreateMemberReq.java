package com.architrave.portfolio.api.dto.auth.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberReq{
    private String email;
    private String password;
    private String username;
}