package com.architrave.portfolio.api.dto.project.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectInfoReq {
    private Long projectInfoId;
    private String customName;
    private String customValue;
}
