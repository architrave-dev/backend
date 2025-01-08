package com.architrave.portfolio.api.dto.setting.response;

import com.architrave.portfolio.domain.model.*;
import com.architrave.portfolio.domain.model.enumType.WorkType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SettingDto {
    private Long id;

    // ----- Setting 관련 -----
    private String pageName;
    private Boolean pageVisible;
    private MenuVisible menuVisible;

    // ----- Member 관련 -----
    private String email;
    private String aui;

    public SettingDto(Setting setting, Member member) {
        this.id = setting.getId();
        this.pageName = setting.getPageName();
        this.pageVisible = setting.getPageVisible();
        this.menuVisible = setting.getMenuVisible();
        this.email = member.getEmail();
        this.aui = member.getAui();
    }
}
