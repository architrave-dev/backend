package com.architrave.portfolio.api.dto.projectElement.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectElementListReq {

    @NotNull
    private Long projectId;
    private List<IndexDto> peIndexList;
    private List<UpdateProjectElementReq> updatedProjectElements;
    private List<DeleteProjectElementReq> removedProjectElements;

}
