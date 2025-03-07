package com.architrave.portfolio.api.dto.career.response;


import com.architrave.portfolio.domain.model.Career;
import com.architrave.portfolio.domain.model.enumType.CareerType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CareerDto {
    private Long id;
    private CareerType careerType;
    private Integer yearFrom;
    private String content;

    public CareerDto(Career career) {
        this.id = career.getId();
        this.careerType = career.getCareerType();
        this.yearFrom = career.getYearFrom();
        this.content = career.getContent();
    }
}
