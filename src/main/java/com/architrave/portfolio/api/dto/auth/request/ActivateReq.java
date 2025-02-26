package com.architrave.portfolio.api.dto.auth.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivateReq {
    @NotEmpty
    private String key;
    @NotEmpty
    private String verificationCode;
}
