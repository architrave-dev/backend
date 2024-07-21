package com.architrave.portfolio.api.dto.projectElement.response;

import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.TextBox;
import com.architrave.portfolio.domain.model.Work;
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
    private Work work;
    private WorkAlignment workAlignment;
    private TextBox textBox;
    private TextBoxAlignment textBoxAlignment;
    private DividerType dividerType;
    private Integer order;
    private Boolean isRepresentative;

    public ProjectElementDto(ProjectElement projectElement){
        this.id = projectElement.getId();
        //work
        this.work = projectElement.getWork();
        this.workAlignment = projectElement.getWorkAlignment();
        //textbox
        this.textBox = projectElement.getTextBox();
        this.textBoxAlignment = projectElement.getTextBoxAlignment();
        //divider
        this.dividerType = projectElement.getDividerType();
        this.order = projectElement.getPeOrder();
        this.isRepresentative = projectElement.getIsRepresentative();
    }
}
