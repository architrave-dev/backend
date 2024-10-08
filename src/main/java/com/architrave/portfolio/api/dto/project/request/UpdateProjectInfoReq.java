package com.architrave.portfolio.api.dto.project.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectInfoReq {
    @NotNull
    private Long id;
    private String customName;
    private String customValue;
}
