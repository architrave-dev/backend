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

    public static ProjectInfo createProjectInfo(Project project, String name, String value){
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.project = project;
        projectInfo.customName = name;
        projectInfo.customValue = value;
        return projectInfo;
    }

    /**
     * ProjectInfoReq 에서 ProjectInfo 로 변환하는 메소드
     */
    public static ProjectInfo convertChamber(Long id, String name, String value) {
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.id = id;
        projectInfo.customName = name;
        projectInfo.customValue = value;
        return projectInfo;
    }
}
