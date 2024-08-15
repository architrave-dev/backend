package com.architrave.portfolio.api.dto.project.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectInfoReq {
    @NotEmpty
    private String customName;
    @NotEmpty
    private String customValue;
}
