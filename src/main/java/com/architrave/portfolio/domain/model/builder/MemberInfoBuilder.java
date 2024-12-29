package com.architrave.portfolio.domain.model.builder;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.MemberInfo;
import com.architrave.portfolio.domain.model.SocialMedia;
import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.enumType.CountryType;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;

public class MemberInfoBuilder {

    private Member member;
    private String originUrl;
    private String thumbnailUrl;
    private String name;
    private CountryType country;
    private Integer year;
    private String email;
    private String contact;
    private String description;
    private String address;
    private SocialMedia sns;

    public MemberInfoBuilder member(Member member){
        this.member = member;
        return this;
    }
    public MemberInfoBuilder originUrl(String originUrl){
        this.originUrl = originUrl;
        return this;
    }
    public MemberInfoBuilder thumbnailUrl(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }
    public MemberInfoBuilder name(String name){
        this.name = name;
        return this;
    }
    public MemberInfoBuilder country(CountryType country){
        this.country = country;
        return this;
    }
    public MemberInfoBuilder year(Integer year){
        this.year = year;
        return this;
    }
    public MemberInfoBuilder email(String email){
        this.email = email;
        return this;
    }
    public MemberInfoBuilder contact(String contact){
        this.contact = contact;
        return this;
    }
    public MemberInfoBuilder description(String description){
        this.description = description;
        return this;
    }
    public MemberInfoBuilder address(String address){
        this.address = address;
        return this;
    }
    public MemberInfoBuilder sns(SocialMedia sns){
        this.sns = sns;
        return this;
    }


    /**
     * originUrl과 thumbnailUrl는 필수값 입니다. <br/>
     * originUrl과 thumbnailUrl로 UploadFile을 생성합니다.
     * @return MemberInfo
     */
    public MemberInfo build(){
        validateMemberInfo();
        //일단 무지성 UploadFile 생성
        UploadFile uploadFile = UploadFile.builder()
                .originUrl(originUrl)
                .thumbnailUrl(thumbnailUrl)
                .build();

        return MemberInfo.createMemberInfo(
                this.member,
                uploadFile,
                this.name,
                this.country,
                this.year,
                this.email,
                this.contact,
                this.description,
                this.address,
                this.sns
        );
    }
    private void validateMemberInfo(){
        if(member == null || originUrl == null || thumbnailUrl == null || name == null){
            throw new RequiredValueEmptyException("required value is empty in MemberInfoBuilder");
        }
    }
}
