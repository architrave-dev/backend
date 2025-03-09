package com.architrave.portfolio.domain.model.builder.projectElementBuilder;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.TextBox;
import com.architrave.portfolio.domain.model.enumType.TextAlignment;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TextBoxInPEBuilder {
    private Project project;
    private TextBox textBox;
    private TextAlignment textAlignment;
    private Integer index;

    public TextBoxInPEBuilder project(Project project){
        this.project = project;
        return this;
    }

    public TextBoxInPEBuilder textBox(TextBox textBox){
        this.textBox = textBox;
        return this;
    }
    public TextBoxInPEBuilder textBoxAlignment(TextAlignment textAlignment){
        this.textAlignment = textAlignment;
        return this;
    }

    public TextBoxInPEBuilder index(Integer index){
        this.index = index;
        return this;
    }

    public ProjectElement build(){
        validatePE();
        return ProjectElement.createTextBoxElement(
                project,
                textBox,
                textAlignment,
                index
        );
    }
    private void validatePE(){
        if(project == null || textBox == null || textAlignment == null || index == null){
            throw new RequiredValueEmptyException("required value is empty in TextBoxInPEBuilder");
        }
    }
}
