package com.architrave.portfolio.api.dto.project.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfoReq {
    private Long id;
    private String customName;
    private String customValue;
}
