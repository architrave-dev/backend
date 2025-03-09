package com.architrave.portfolio.domain.model.builder.projectElementBuilder;

import com.architrave.portfolio.domain.model.Document;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.enumType.DisplayAlignment;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DocumentInProjectBuilder {
    private Project project;
    private Document document;
    private DisplayAlignment documentAlignment;
    private Integer index;

    public DocumentInProjectBuilder project(Project project){
        this.project = project;
        return this;
    }

    public DocumentInProjectBuilder document(Document document){
        this.document = document;
        return this;
    }
    public DocumentInProjectBuilder documentAlignment(DisplayAlignment documentAlignment){
        this.documentAlignment = documentAlignment;
        return this;
    }

    public DocumentInProjectBuilder index(Integer index){
        this.index = index;
        return this;
    }

    public ProjectElement build(){
        validateProject();
        return ProjectElement.createDocumentElement(
                project,
                document,
                documentAlignment,
                index
        );
    }
    private void validateProject(){
        if(project == null || document == null || documentAlignment == null || index == null ){
            throw new RequiredValueEmptyException("required value is empty in ProjectBuilder");
        }
    }
}
