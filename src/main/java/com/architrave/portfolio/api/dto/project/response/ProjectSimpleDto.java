package com.architrave.portfolio.api.dto.project.response;

import com.architrave.portfolio.domain.model.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectSimpleDto {
    private Long id;
    private String title;
    private String description;

    public ProjectSimpleDto(Project project){
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
    }
}