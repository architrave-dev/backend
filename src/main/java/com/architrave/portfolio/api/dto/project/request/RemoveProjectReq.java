package com.architrave.portfolio.api.dto.project.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RemoveProjectReq {
    @NotNull
    private Long projectId;
}
