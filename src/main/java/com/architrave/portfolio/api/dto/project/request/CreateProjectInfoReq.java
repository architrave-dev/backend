package com.architrave.portfolio.api.dto.project.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectInfoReq {

    @NotNull
    private Long tempId;
    @NotNull
    private Long projectId;
    @NotEmpty
    private String customName;
    @NotEmpty
    private String customValue;
}
