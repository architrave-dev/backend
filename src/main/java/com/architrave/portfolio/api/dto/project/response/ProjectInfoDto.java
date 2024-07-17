package com.architrave.portfolio.api.dto.project.response;

import com.architrave.portfolio.domain.model.ProjectInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfoDto {
    private Long id;
    private String customName;
    private String customValue;

    public ProjectInfoDto(ProjectInfo projectInfo){
        this.id = projectInfo.getId();
        this.customName = projectInfo.getCustomName();
        this.customValue = projectInfo.getCustomValue();
    }
}
