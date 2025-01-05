package com.architrave.portfolio.domain.model.builder;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.MenuVisible;
import com.architrave.portfolio.domain.model.Setting;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;

import java.awt.*;

public class SettingBuilder {

    private Member member;
    private String pageName;
    private Boolean pageVisible;
    private MenuVisible menuVisible;


    public SettingBuilder member(Member member){
        this.member = member;
        return this;
    }
    public SettingBuilder pageName(String pageName){
        this.pageName = pageName;
        return this;
    }
    public SettingBuilder pageVisible(Boolean pageVisible){
        this.pageVisible = pageVisible;
        return this;
    }
    public SettingBuilder menuVisible(MenuVisible menuVisible){
        this.menuVisible = menuVisible;
        return this;
    }

    /**
     * originUrl과 thumbnailUrl는 필수값 입니다. <br/>
     * originUrl과 thumbnailUrl로 UploadFile을 생성합니다.
     * @return Billboard
     */
    public Setting build(){
        validateSetting();
        return Setting.createSetting(
                this.member,
                this.pageName,
                this.pageVisible,
                this.menuVisible
        );
    }
    private void validateSetting(){
        if(member == null || pageName == null || pageVisible == null || menuVisible == null ){
            throw new RequiredValueEmptyException("required value is empty in SettingBuilder");
        }
    }
}
