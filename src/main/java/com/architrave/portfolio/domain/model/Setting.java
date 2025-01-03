package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Setting {

    @Id
    @GeneratedValue
    @Column(name = "setting_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String pageName;
    private Boolean pageVisible;

    @Embedded
    private MenuVisible menuVisible;

    public static Setting createSetting(Member member, String pageName,  Boolean pageVisible, Boolean projects, Boolean works, Boolean about, Boolean contact) {

        Setting setting = new Setting();
        setting.member = member;
        setting.pageName = pageName;
        setting.pageVisible = pageVisible;
        MenuVisible menuVisible = new MenuVisible(
                projects,
                works,
                about,
                contact
        );
        setting.menuVisible = menuVisible;

        return setting;
    }

    //여기에 subscription 관련 추가...?




}
