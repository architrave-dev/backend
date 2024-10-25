package com.architrave.portfolio.api.dto.career.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCareerReq {
    @NotNull
    private Long careerId;
    private Integer yearFrom;
    @NotNull
    private String content;
}
