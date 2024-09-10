package com.architrave.portfolio.api.dto.career.response;

import com.architrave.portfolio.api.dto.auth.response.MemberSimpleDto;
import com.architrave.portfolio.domain.model.Career;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.enumType.CareerType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CareerDto {
    private Long id;
    private CareerType careerType;
    private Integer yearFrom;
    private Integer yearTo;
    private String content;
    private Integer index;

    public CareerDto(Career career) {
        this.id = career.getId();
        this.careerType = career.getCareerType();
        this.yearFrom = career.getYearFrom();
        this.yearTo = career.getYearTo();
        this.content = career.getContent();
//        this.index = index;
    }
}
