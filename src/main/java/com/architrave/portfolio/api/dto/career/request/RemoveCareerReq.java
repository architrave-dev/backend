package com.architrave.portfolio.api.dto.career.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RemoveCareerReq {

    @NotNull
    private Long careerId;
}
