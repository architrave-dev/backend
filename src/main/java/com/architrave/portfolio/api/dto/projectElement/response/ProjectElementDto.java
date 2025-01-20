package com.architrave.portfolio.api.dto.projectElement.response;

import com.architrave.portfolio.api.dto.document.response.DocumentDto;
import com.architrave.portfolio.api.dto.work.response.WorkDetailDto;
import com.architrave.portfolio.api.dto.work.response.WorkDto;
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
    private WorkDetailDto workDetail;
    private TextBox textBox;
    private DocumentDto document;

    private DisplayAlignment displayAlignment;
    private DisplaySize displaySize;
    private TextAlignment textAlignment;
    private DividerType dividerType;

    public ProjectElementDto(ProjectElement projectElement){
        this.id = projectElement.getId();
        this.work = (projectElement.getWork() != null) ?  new WorkDto(projectElement.getWork()) : null;
        this.workDetail = (projectElement.getWorkDetail() != null) ?  new WorkDetailDto(projectElement.getWorkDetail()) : null;
        this.textBox = projectElement.getTextBox();
        this.document = (projectElement.getDocument() != null) ? new DocumentDto(projectElement.getDocument()): null;
        this.dividerType = projectElement.getDividerType();


        if(this.work != null){
            this.projectElementType = ProjectElementType.WORK;
            this.displayAlignment = projectElement.getDisplayAlignment();
            this.displaySize = projectElement.getDisplaySize();
        }else if(this.workDetail != null){
            this.projectElementType = ProjectElementType.DETAIL;
            this.displayAlignment = projectElement.getDisplayAlignment();
            this.displaySize = projectElement.getDisplaySize();
        }else if(this.textBox != null){
            this.projectElementType = ProjectElementType.TEXTBOX;
            this.textAlignment = projectElement.getTextAlignment();
        }else if(this.document != null){
            this.projectElementType = ProjectElementType.DOCUMENT;
            this.displayAlignment = projectElement.getDisplayAlignment();
        }else if(this.dividerType != null){
            this.projectElementType = ProjectElementType.DIVIDER;
        }
    }
}
