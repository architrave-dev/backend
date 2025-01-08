package com.architrave.portfolio.api.dto.setting.request;

import com.architrave.portfolio.domain.model.MenuVisible;
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
    private MenuVisible menuVisible;
}
