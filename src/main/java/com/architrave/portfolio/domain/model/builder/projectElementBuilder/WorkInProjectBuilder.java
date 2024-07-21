package com.architrave.portfolio.domain.model.builder.projectElementBuilder;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WorkInProjectBuilder {
    private Project project;
    private Work work;
    private WorkAlignment workAlignment;
    private Integer peOrder;
    private Boolean isRepresentative;

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

    public WorkInProjectBuilder peOrder(Integer peOrder){
        this.peOrder = peOrder;
        return this;
    }

    public WorkInProjectBuilder isRepresentative(Boolean isRepresentative){
        this.isRepresentative = isRepresentative;
        return this;
    }
    public ProjectElement build(){
        validateProject();
        return ProjectElement.createWorkElement(
                project,
                work,
                workAlignment,
                peOrder,
                isRepresentative
        );
    }
    private void validateProject(){
        if(project == null || work == null || workAlignment == null || peOrder == null || isRepresentative == null){
            throw new IllegalArgumentException("required value is empty in ProjectBuilder");
        }
    }
}
