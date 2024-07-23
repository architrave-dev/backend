package com.architrave.portfolio.api.dto.project.response;

import com.architrave.portfolio.api.dto.projectElement.response.ProjectElementDto;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.ProjectInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String supportedBy;
    private List<ProjectInfoDto> projectInfoList;
    private List<ProjectElementDto> projectElementList;
    private Boolean isDeleted;

    public ProjectDto(Project project,
                      List<ProjectInfoDto> projectInfoList,
                      List<ProjectElementDto>  projectElementList){
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
        this.supportedBy = project.getSupportedBy();
        this.projectInfoList = projectInfoList;
        this.projectElementList = projectElementList;
        this.isDeleted = project.getIsDeleted();
    }
}
