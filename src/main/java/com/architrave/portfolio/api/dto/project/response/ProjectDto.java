package com.architrave.portfolio.api.dto.project.response;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.UploadFile;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectDto {
    private Long id;
    private String title;
    private String description;
    private UploadFile uploadFile;
    private String piIndex;
    private List<ProjectInfoDto> projectInfoList;

    public ProjectDto(Project project,
                      List<ProjectInfoDto> projectInfoList){
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.uploadFile = project.getUploadFile();
        this.piIndex = project.getPiIndex();
        this.projectInfoList = projectInfoList;
    }
}
