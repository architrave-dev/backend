package com.architrave.portfolio.domain.model.builder;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Setting;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;

public class SettingBuilder {

    private Member member;
    private String pageName;
    private Boolean pageVisible;
    private Boolean projects;
    private Boolean works;
    private Boolean about;
    private Boolean contact;


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
    public SettingBuilder projects(Boolean projects){
        this.projects = projects;
        return this;
    }
    public SettingBuilder works(Boolean works){
        this.works = works;
        return this;
    }
    public SettingBuilder about(Boolean about){
        this.about = about;
        return this;
    }
    public SettingBuilder contact(Boolean contact){
        this.contact = contact;
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
                this.projects,
                this.works,
                this.about,
                this.contact
        );
    }
    private void validateSetting(){
        if(member == null || pageName == null || pageVisible == null || projects == null || works == null || about == null || contact == null){
            throw new RequiredValueEmptyException("required value is empty in BillboardBuilder");
        }
    }
}
