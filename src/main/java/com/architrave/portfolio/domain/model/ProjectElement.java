package com.architrave.portfolio.domain.model;

import com.architrave.portfolio.domain.model.enumType.DividerType;
import com.architrave.portfolio.domain.model.enumType.ProjectElementType;
import com.architrave.portfolio.domain.model.enumType.TextBoxAlignment;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProjectElement {

    @Id
    @GeneratedValue
    @Column(name = "project_element_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProjectElementType projectElementType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToOne
    @JoinColumn(name = "work_id")
    private Work work;

    @Enumerated(EnumType.STRING)
    private WorkAlignment workAlignment;

    @OneToOne
    @JoinColumn(name = "text_box_id")
    private TextBox textBox;

    @Enumerated(EnumType.STRING)
    private TextBoxAlignment textBoxAlignment;

    @Enumerated
    private DividerType dividerType;

    @Column
    private Integer peOrder;

    public static ProjectElement createWorkElement(
            Project project,
            Work work,
            WorkAlignment workAlignment,
            Integer peOrder
    ){
        ProjectElement projectElement = new ProjectElement();
        projectElement.projectElementType = ProjectElementType.WORK;
        projectElement.project = project;
        projectElement.work = work;
        projectElement.workAlignment = workAlignment;
        projectElement.peOrder = peOrder;
        return projectElement;
    }
    public static ProjectElement createTextBoxElement(
            Project project,
            TextBox textBox,
            TextBoxAlignment textBoxAlignment,
            Integer peOrder
    ){
        ProjectElement projectElement = new ProjectElement();
        projectElement.projectElementType = ProjectElementType.TEXTBOX;
        projectElement.project = project;
        projectElement.textBox = textBox;
        projectElement.textBoxAlignment = textBoxAlignment;
        projectElement.peOrder = peOrder;
        return projectElement;
    }
    public static ProjectElement createDividerElement(
            Project project,
            DividerType dividerType,
            Integer peOrder
    ){
        ProjectElement projectElement = new ProjectElement();
        projectElement.projectElementType = ProjectElementType.DIVIDER;
        projectElement.project = project;
        projectElement.dividerType = dividerType;
        projectElement.peOrder = peOrder;
        return projectElement;
    }
}
