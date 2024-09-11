package com.architrave.portfolio.domain.model.builder.projectElementBuilder;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.enumType.DividerType;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DividerInProjectBuilder {
    private Project project;
    private DividerType dividerType;

    public DividerInProjectBuilder project(Project project){
        this.project = project;
        return this;
    }

    public DividerInProjectBuilder dividerType(DividerType dividerType){
        this.dividerType = dividerType;
        return this;
    }

    public ProjectElement build(){
        validateProject();
        return ProjectElement.createDividerElement(
                project,
                dividerType
        );
    }
    private void validateProject(){
        if(project == null || dividerType == null ){
            throw new RequiredValueEmptyException("required value is empty in ProjectBuilder");
        }
    }
}
