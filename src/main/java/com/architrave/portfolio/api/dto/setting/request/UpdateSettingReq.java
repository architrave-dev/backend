package com.architrave.portfolio.api.dto.setting.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSettingReq {
    @NotNull
    private Long id;
    private String pageName;
    private Boolean pageVisible;
    private Boolean projects;
    private Boolean works;
    private Boolean about;
    private Boolean contact;
}
