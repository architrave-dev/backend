package com.architrave.portfolio.domain.model.builder.projectElementBuilder;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.WorkDetail;
import com.architrave.portfolio.domain.model.enumType.DisplayAlignment;
import com.architrave.portfolio.domain.model.enumType.DisplaySize;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WorkDetailInPEBuilder {
    private Project project;
    private WorkDetail workDetail;
    private DisplayAlignment workDetailAlignment;
    private DisplaySize workDetailDisplaySize;
    private Integer index;

    public WorkDetailInPEBuilder project(Project project){
        this.project = project;
        return this;
    }

    public WorkDetailInPEBuilder workDetail(WorkDetail workDetail){
        this.workDetail = workDetail;
        return this;
    }
    public WorkDetailInPEBuilder workDetailAlignment(DisplayAlignment workDetailAlignment){
        this.workDetailAlignment = workDetailAlignment;
        return this;
    }
    public WorkDetailInPEBuilder workDetailDisplaySize(DisplaySize workDetailDisplaySize){
        this.workDetailDisplaySize = workDetailDisplaySize;
        return this;
    }
    public WorkDetailInPEBuilder index(Integer index){
        this.index = index;
        return this;
    }

    public ProjectElement build(){
        validatePE();
        return ProjectElement.createWorkDetailElement(
                project,
                workDetail,
                workDetailAlignment,
                workDetailDisplaySize,
                index
        );
    }
    private void validatePE(){
        if(project == null || workDetail == null || workDetailAlignment == null || workDetailDisplaySize == null || index == null){
            throw new RequiredValueEmptyException("required value is empty in WorkDetailInPEBuilder");
        }
    }
}
