package com.architrave.portfolio.domain.model.builder.projectElementBuilder;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.enumType.DisplayAlignment;
import com.architrave.portfolio.domain.model.enumType.DisplaySize;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WorkInProjectBuilder {
    private Project project;
    private Work work;
    private DisplayAlignment displayAlignment;
    private DisplaySize displaySize;

    public WorkInProjectBuilder project(Project project){
        this.project = project;
        return this;
    }

    public WorkInProjectBuilder work(Work work){
        this.work = work;
        return this;
    }
    public WorkInProjectBuilder workAlignment(DisplayAlignment displayAlignment){
        this.displayAlignment = displayAlignment;
        return this;
    }
    public WorkInProjectBuilder workDisplaySize(DisplaySize displaySize){
        this.displaySize = displaySize;
        return this;
    }

    public ProjectElement build(){
        validateProject();
        return ProjectElement.createWorkElement(
                project,
                work,
                displayAlignment,
                displaySize
        );
    }
    private void validateProject(){
        if(project == null || work == null || displayAlignment == null || displaySize == null){
            throw new RequiredValueEmptyException("required value is empty in ProjectBuilder");
        }
    }
}
