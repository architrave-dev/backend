package com.architrave.portfolio.api.dto.auth.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordReq {

    @NotNull
    private Long id;
    private String password;
    private String newPassword;
}