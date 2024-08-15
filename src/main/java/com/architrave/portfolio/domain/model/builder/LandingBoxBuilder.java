package com.architrave.portfolio.domain.model.builder;

import com.architrave.portfolio.domain.model.LandingBox;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;

public class LandingBoxBuilder {

    private Member member;
    private String originImgUrl;
    private String thumbnailUrl;
    private String title;
    private String description;

    public LandingBoxBuilder member(Member member){
        this.member = member;
        return this;
    }

    public LandingBoxBuilder title(String title){
        this.title = title;
        return this;
    }
    public LandingBoxBuilder description(String description){
        this.description = description;
        return this;
    }
    public LandingBoxBuilder originImgUrl(String originImgUrl){
        this.originImgUrl = originImgUrl;
        return this;
    }
    public LandingBoxBuilder thumbnailUrl(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    /**
     * originImgUrl과 thumbnailUrl는 필수값 입니다. <br/>
     * originImgUrl과 thumbnailUrl로 UploadFile을 생성합니다.
     * @return LandingBox
     */
    public LandingBox build(){
        validateLandingBox();
        //일단 무지성 UploadFile 생성
        UploadFile uploadFile = UploadFile.builder()
                .originUrl(originImgUrl)
                .thumbnailUrl(thumbnailUrl)
                .build();

        return LandingBox.createLandingBox(
                this.member,
                uploadFile,
                this.title,
                this.description
        );
    }
    private void validateLandingBox(){
        if(member == null || originImgUrl == null || thumbnailUrl == null){
            throw new RequiredValueEmptyException("required value is empty in LandingBoxBuilder");
        }
    }
}
