package com.architrave.portfolio.domain.model.builder.projectElementBuilder;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.WorkDetail;
import com.architrave.portfolio.domain.model.enumType.DisplayAlignment;
import com.architrave.portfolio.domain.model.enumType.DisplaySize;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WorkDetailInProjectBuilder {
    private Project project;
    private WorkDetail workDetail;
    private DisplayAlignment workDetailAlignment;
    private DisplaySize workDetailDisplaySize;
    private Integer index;

    public WorkDetailInProjectBuilder project(Project project){
        this.project = project;
        return this;
    }

    public WorkDetailInProjectBuilder workDetail(WorkDetail workDetail){
        this.workDetail = workDetail;
        return this;
    }
    public WorkDetailInProjectBuilder workDetailAlignment(DisplayAlignment workDetailAlignment){
        this.workDetailAlignment = workDetailAlignment;
        return this;
    }
    public WorkDetailInProjectBuilder workDetailDisplaySize(DisplaySize workDetailDisplaySize){
        this.workDetailDisplaySize = workDetailDisplaySize;
        return this;
    }
    public WorkDetailInProjectBuilder index(Integer index){
        this.index = index;
        return this;
    }

    public ProjectElement build(){
        validateProject();
        return ProjectElement.createWorkDetailElement(
                project,
                workDetail,
                workDetailAlignment,
                workDetailDisplaySize,
                index
        );
    }
    private void validateProject(){
        if(project == null || workDetail == null || workDetailAlignment == null || workDetailDisplaySize == null || index == null){
            throw new RequiredValueEmptyException("required value is empty in ProjectBuilder");
        }
    }
}
