package com.architrave.portfolio.api.dto.projectElement.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProjectElementReq {
    @NotNull
    private Long projectElementId;
}
