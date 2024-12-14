package com.architrave.portfolio.api.dto.projectElement.response;

import com.architrave.portfolio.api.dto.document.response.DocumentDto;
import com.architrave.portfolio.api.dto.work.response.WorkDto;
import com.architrave.portfolio.domain.model.Document;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.TextBox;
import com.architrave.portfolio.domain.model.enumType.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectElementDto {
    private Long id;
    private ProjectElementType projectElementType;
    private WorkDto work;
    private WorkAlignment workAlignment;
    private WorkDisplaySize workDisplaySize;
    private TextBox textBox;
    private TextBoxAlignment textBoxAlignment;
    private DocumentDto document;
    private WorkAlignment documentAlignment;
    private DividerType dividerType;

    public ProjectElementDto(ProjectElement projectElement){
        this.id = projectElement.getId();
        //work
        this.work = (projectElement.getWork() != null) ?  new WorkDto(projectElement.getWork()) : null;

        this.workAlignment = projectElement.getWorkAlignment();
        this.workDisplaySize = projectElement.getWorkDisplaySize();
        //textbox
        this.textBox = projectElement.getTextBox();
        this.textBoxAlignment = projectElement.getTextBoxAlignment();
        //document
        this.document = (projectElement.getDocument() != null) ? new DocumentDto(projectElement.getDocument()): null;
        this.documentAlignment = projectElement.getDocumentAlignment();
        //divider
        this.dividerType = projectElement.getDividerType();
        if(this.work != null){
            this.projectElementType = ProjectElementType.WORK;
        }else if(this.textBox != null){
            this.projectElementType = ProjectElementType.TEXTBOX;
        }else if(this.document != null){
            this.projectElementType = ProjectElementType.DOCUMENT;
        }else if(this.dividerType != null){
            this.projectElementType = ProjectElementType.DIVIDER;
        }
    }
}
