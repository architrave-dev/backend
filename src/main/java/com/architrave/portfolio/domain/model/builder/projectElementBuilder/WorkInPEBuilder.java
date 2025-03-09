package com.architrave.portfolio.domain.model.builder.projectElementBuilder;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.enumType.DisplayAlignment;
import com.architrave.portfolio.domain.model.enumType.DisplaySize;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WorkInPEBuilder {
    private Project project;
    private Work work;
    private DisplayAlignment displayAlignment;
    private DisplaySize displaySize;
    private Integer index;

    public WorkInPEBuilder project(Project project){
        this.project = project;
        return this;
    }

    public WorkInPEBuilder work(Work work){
        this.work = work;
        return this;
    }
    public WorkInPEBuilder workAlignment(DisplayAlignment displayAlignment){
        this.displayAlignment = displayAlignment;
        return this;
    }
    public WorkInPEBuilder workDisplaySize(DisplaySize displaySize){
        this.displaySize = displaySize;
        return this;
    }
    public WorkInPEBuilder index(Integer index){
        this.index = index;
        return this;
    }

    public ProjectElement build(){
        validatePE();
        return ProjectElement.createWorkElement(
                project,
                work,
                displayAlignment,
                displaySize,
                index
        );
    }
    private void validatePE(){
        if(project == null || work == null || displayAlignment == null || displaySize == null || index == null){
            throw new RequiredValueEmptyException("required value is empty in WorkInPEBuilder");
        }
    }
}
