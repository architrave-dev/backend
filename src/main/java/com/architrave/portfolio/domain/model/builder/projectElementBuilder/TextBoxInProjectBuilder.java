package com.architrave.portfolio.domain.model.builder.projectElementBuilder;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.TextBox;
import com.architrave.portfolio.domain.model.enumType.TextAlignment;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TextBoxInProjectBuilder {
    private Project project;
    private TextBox textBox;
    private TextAlignment textAlignment;
    private Integer index;

    public TextBoxInProjectBuilder project(Project project){
        this.project = project;
        return this;
    }

    public TextBoxInProjectBuilder textBox(TextBox textBox){
        this.textBox = textBox;
        return this;
    }
    public TextBoxInProjectBuilder textBoxAlignment(TextAlignment textAlignment){
        this.textAlignment = textAlignment;
        return this;
    }

    public TextBoxInProjectBuilder index(Integer index){
        this.index = index;
        return this;
    }

    public ProjectElement build(){
        validateProject();
        return ProjectElement.createTextBoxElement(
                project,
                textBox,
                textAlignment,
                index
        );
    }
    private void validateProject(){
        if(project == null || textBox == null || textAlignment == null || index == null){
            throw new RequiredValueEmptyException("required value is empty in ProjectBuilder");
        }
    }
}
