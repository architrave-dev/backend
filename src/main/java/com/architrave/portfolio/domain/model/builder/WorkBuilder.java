package com.architrave.portfolio.domain.model.builder;

import com.architrave.portfolio.domain.model.*;
import com.architrave.portfolio.domain.model.enumType.WorkType;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WorkBuilder {

    private Member member;
    private WorkType workType;
    private String originUrl;
    private String thumbnailUrl;
    private String title;
    private String description;
    private Size size;
    private String material;
    private Integer prodYear;
    private String price;
    private String collection;

    public WorkBuilder member(Member member) {
        this.member = member;
        return this;
    }
    public WorkBuilder workType(WorkType workType) {
        this.workType = workType;
        return this;
    }
    public WorkBuilder title(String title){
        this.title = title;
        return this;
    }
    public WorkBuilder description(String description){
        this.description = description;
        return this;
    }
    public WorkBuilder originUrl(String originUrl){
        this.originUrl = originUrl;
        return this;
    }
    public WorkBuilder thumbnailUrl(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }
    public WorkBuilder size(Size size){
        this.size = size;
        return this;
    }
    public WorkBuilder material(String material){
        this.material = material;
        return this;
    }
    public WorkBuilder prodYear(Integer prodYear){
        this.prodYear = prodYear;
        return this;
    }
    public WorkBuilder price(String price){
        this.price = price;
        return this;
    }
    public WorkBuilder collection(String collection){
        this.collection = collection;
        return this;
    }

    /**
     * originUrl과 thumbnailUrl는 필수값 입니다. <br/>
     * originUrl과 thumbnailUrl로 UploadFile을 생성합니다.
     * @return Billboard
     */
    public Work build(){
        validateWork();
        //일단 무지성 UploadFile 생성
        UploadFile uploadFile = UploadFile.builder()
                .originUrl(originUrl)
                .thumbnailUrl(thumbnailUrl)
                .build();

        return Work.createWork(
                this.member,
                this.workType,
                uploadFile,
                this.title,
                this.description,
                this.size,
                this.material,
                this.prodYear,
                this.price,
                this.collection
        );
    }
    private void validateWork(){
        if(member == null|| workType == null || originUrl == null || thumbnailUrl == null){
            throw new RequiredValueEmptyException("required value is empty in WorkBuilder");
        }
    }
}
