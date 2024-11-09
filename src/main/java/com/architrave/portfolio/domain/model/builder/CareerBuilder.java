package com.architrave.portfolio.domain.model.builder;

import com.architrave.portfolio.domain.model.Career;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.enumType.CareerType;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;


public class CareerBuilder {
    private Member member;
    private CareerType careerType;
    private Integer yearFrom;
    private String content;

    public CareerBuilder member(Member member) {
        this.member = member;
        return this;
    }
    public CareerBuilder careerType(CareerType careerType) {
        this.careerType = careerType;
        return this;
    }
    public CareerBuilder content(String content) {
        this.content = content;
        return this;
    }
    public CareerBuilder yearFrom(Integer yearFrom) {
        this.yearFrom = yearFrom;
        return this;
    }

    public Career build(){
        validateCareer();
        return Career.createCareer(
                this.member,
                this.careerType,
                this.yearFrom,
                this.content
        );
    }
    private void validateCareer(){
        if(member == null || careerType == null || content == null || yearFrom == null){
            throw new RequiredValueEmptyException("required value is empty in CareerBuilder");
        }
    }
}
