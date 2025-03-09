package com.architrave.portfolio.api.dto.project.request;

import com.architrave.portfolio.api.dto.reorder.request.IndexDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectInfoReq extends IndexDto {

    @NotNull
    private Long projectId;
    private String customName;
    private String customValue;
}
