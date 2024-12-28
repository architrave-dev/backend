package com.architrave.portfolio.domain.model.builder.projectElementBuilder;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.WorkDetail;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import com.architrave.portfolio.domain.model.enumType.WorkDisplaySize;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WorkDetailInProjectBuilder {
    private Project project;
    private WorkDetail workDetail;
    private WorkAlignment workDetailAlignment;
    private WorkDisplaySize workDetailDisplaySize;

    public WorkDetailInProjectBuilder project(Project project){
        this.project = project;
        return this;
    }

    public WorkDetailInProjectBuilder workDetail(WorkDetail workDetail){
        this.workDetail = workDetail;
        return this;
    }
    public WorkDetailInProjectBuilder workDetailAlignment(WorkAlignment workDetailAlignment){
        this.workDetailAlignment = workDetailAlignment;
        return this;
    }
    public WorkDetailInProjectBuilder workDetailDisplaySize(WorkDisplaySize workDetailDisplaySize){
        this.workDetailDisplaySize = workDetailDisplaySize;
        return this;
    }

    public ProjectElement build(){
        validateProject();
        return ProjectElement.createWorkDetailElement(
                project,
                workDetail,
                workDetailAlignment,
                workDetailDisplaySize
        );
    }
    private void validateProject(){
        if(project == null || workDetail == null || workDetailAlignment == null || workDetailDisplaySize == null){
            throw new RequiredValueEmptyException("required value is empty in ProjectBuilder");
        }
    }
}
