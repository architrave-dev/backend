package com.architrave.portfolio.api.dto.auth.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshReq {
    @NotEmpty
    private String refreshToken;
}
