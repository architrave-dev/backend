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
    private Boolean projects;
    private Boolean works;
    private Boolean about;
    private Boolean contact;

    // ----- Member 관련 -----
    private String email;
    private String aui;
    private String password;

    public SettingDto(Setting setting, Member member) {
        this.pageName = setting.getPageName();
        this.pageVisible = setting.getPageVisible();
        this.projects = setting.getMenuVisible().getProjects();
        this.works = setting.getMenuVisible().getWorks();
        this.about = setting.getMenuVisible().getAbout();
        this.contact = setting.getMenuVisible().getContact();

        this.email = member.getEmail();
        this.aui = member.getAui();
        this.password = member.getPassword();
    }
}
