package com.architrave.portfolio.domain.model.builder.projectElementBuilder;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.enumType.DividerType;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DividerInPEBuilder {
    private Project project;
    private DividerType dividerType;
    private Integer index;

    public DividerInPEBuilder project(Project project){
        this.project = project;
        return this;
    }

    public DividerInPEBuilder dividerType(DividerType dividerType){
        this.dividerType = dividerType;
        return this;
    }
    public DividerInPEBuilder index(Integer index){
        this.index = index;
        return this;
    }

    public ProjectElement build(){
        validatePE();
        return ProjectElement.createDividerElement(
                project,
                dividerType,
                index
        );
    }
    private void validatePE(){
        if(project == null || dividerType == null || index == null){
            throw new RequiredValueEmptyException("required value is empty in DividerInPEBuilder");
        }
    }
}
