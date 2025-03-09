package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfo {

    @Id
    @GeneratedValue
    @Column(name = "project_info_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String customName;
    private String customValue;

    private Integer index;

    public static ProjectInfo createProjectInfo(Project project, String name, String value, Integer index){
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.project = project;
        projectInfo.customName = name;
        projectInfo.customValue = value;
        projectInfo.index = index;
        return projectInfo;
    }
}
