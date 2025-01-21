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

    public ProjectElement build(){
        validateProject();
        return ProjectElement.createDocumentElement(
                project,
                document,
                documentAlignment
        );
    }
    private void validateProject(){
        if(project == null || document == null || documentAlignment == null ){
            throw new RequiredValueEmptyException("required value is empty in ProjectBuilder");
        }
    }
}
