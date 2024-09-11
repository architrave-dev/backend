package com.architrave.portfolio.domain.model.builder.projectElementBuilder;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WorkInProjectBuilder {
    private Project project;
    private Work work;
    private WorkAlignment workAlignment;

    public WorkInProjectBuilder project(Project project){
        this.project = project;
        return this;
    }

    public WorkInProjectBuilder work(Work work){
        this.work = work;
        return this;
    }
    public WorkInProjectBuilder workAlignment(WorkAlignment workAlignment){
        this.workAlignment = workAlignment;
        return this;
    }


    public ProjectElement build(){
        validateProject();
        return ProjectElement.createWorkElement(
                project,
                work,
                workAlignment
        );
    }
    private void validateProject(){
        if(project == null || work == null || workAlignment == null){
            throw new RequiredValueEmptyException("required value is empty in ProjectBuilder");
        }
    }
}
