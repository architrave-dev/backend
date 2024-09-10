package com.architrave.portfolio.api.dto.career.request;

import com.architrave.portfolio.domain.model.enumType.CareerType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCareerReq {
    @NotNull
    private CareerType careerType;
    private Integer yearFrom;
    private Integer yearTo;
    @NotNull
    private String content;
}
