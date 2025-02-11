package com.architrave.portfolio.domain.model.builder;

import com.architrave.portfolio.domain.model.Billboard;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;

public class BillboardBuilder {

    private Member member;
    private String originUrl;
    private String title;
    private String description;

    public BillboardBuilder member(Member member){
        this.member = member;
        return this;
    }

    public BillboardBuilder title(String title){
        this.title = title;
        return this;
    }
    public BillboardBuilder description(String description){
        this.description = description;
        return this;
    }
    public BillboardBuilder originUrl(String originUrl){
        this.originUrl = originUrl;
        return this;
    }

    /**
     * originUrl는 필수값 입니다. <br/>
     * originUrl로 UploadFile을 생성합니다.
     * @return Billboard
     */
    public Billboard build(){
        validateBillboard();
        //일단 무지성 UploadFile 생성
        UploadFile uploadFile = UploadFile.builder()
                .originUrl(originUrl)
                .build();

        return Billboard.createBillboard(
                this.member,
                uploadFile,
                this.title,
                this.description
        );
    }
    private void validateBillboard(){
        if(member == null || originUrl == null){
            throw new RequiredValueEmptyException("required value is empty in BillboardBuilder");
        }
    }
}
