package com.architrave.portfolio.api.dto.setting.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePwReq {
    @NotNull
    private Long id;
    private String password;
    private String newPassword;
}
