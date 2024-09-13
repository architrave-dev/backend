package com.architrave.portfolio.api.dto.projectElement.response;

import com.architrave.portfolio.api.dto.work.response.WorkDto;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.TextBox;
import com.architrave.portfolio.domain.model.enumType.DividerType;
import com.architrave.portfolio.domain.model.enumType.ProjectElementType;
import com.architrave.portfolio.domain.model.enumType.TextBoxAlignment;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectElementDto {
    private Long id;
    private ProjectElementType projectElementType;
    private WorkDto work;
    private WorkAlignment workAlignment;
    private TextBox textBox;
    private TextBoxAlignment textBoxAlignment;
    private DividerType dividerType;

    public ProjectElementDto(ProjectElement projectElement){
        this.id = projectElement.getId();
        //work
        this.work = (projectElement.getWork() != null) ?  new WorkDto(projectElement.getWork()) : null;

        this.workAlignment = projectElement.getWorkAlignment();
        //textbox
        this.textBox = projectElement.getTextBox();
        this.textBoxAlignment = projectElement.getTextBoxAlignment();
        //divider
        this.dividerType = projectElement.getDividerType();
        if(this.work != null){
            this.projectElementType = ProjectElementType.WORK;
        }else if(this.textBox != null){
            this.projectElementType = ProjectElementType.TEXTBOX;
        }else if(this.dividerType != null){
            this.projectElementType = ProjectElementType.DIVIDER;
        }
    }
}
