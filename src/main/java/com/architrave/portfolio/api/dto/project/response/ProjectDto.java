package com.architrave.portfolio.api.dto.project.response;

import com.architrave.portfolio.api.dto.projectElement.response.ProjectElementDto;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.UploadFile;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectDto {
    private Long id;
    private String title;
    private String description;
    private String originUrl;
    private String thumbnailUrl;
    private String piIndex;
    private List<ProjectInfoDto> projectInfoList;

    public ProjectDto(Project project,
                      List<ProjectInfoDto> projectInfoList){
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        UploadFile uploadFile = project.getUploadFile();
        this.originUrl = uploadFile.getOriginUrl();
        this.thumbnailUrl = uploadFile.getThumbnailUrl();
        this.piIndex = project.getPiIndex();
        this.projectInfoList = projectInfoList;
    }
}
