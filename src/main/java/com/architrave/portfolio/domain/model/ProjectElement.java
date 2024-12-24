package com.architrave.portfolio.domain.model;

import com.architrave.portfolio.domain.model.enumType.*;
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

    @Enumerated(EnumType.STRING)
    private WorkDisplaySize workDisplaySize;

    @OneToOne
    @JoinColumn(name = "work_detail_id")
    private WorkDetail workDetail;

    @Enumerated(EnumType.STRING)
    private WorkAlignment workDetailAlignment;

    @Enumerated(EnumType.STRING)
    private WorkDisplaySize workDetailDisplaySize;

    @OneToOne
    @JoinColumn(name = "text_box_id")
    private TextBox textBox;

    @Enumerated(EnumType.STRING)
    private TextBoxAlignment textBoxAlignment;

    @OneToOne
    @JoinColumn(name = "document_id")
    private Document document;

    @Enumerated(EnumType.STRING)
    private WorkAlignment documentAlignment;
    @Enumerated(EnumType.STRING)
    private DividerType dividerType;


    public static ProjectElement createWorkElement(
            Project project,
            Work work,
            WorkAlignment workAlignment,
            WorkDisplaySize workDisplaySize
    ){
        ProjectElement projectElement = new ProjectElement();
        projectElement.projectElementType = ProjectElementType.WORK;
        projectElement.project = project;
        projectElement.work = work;
        projectElement.workAlignment = workAlignment;
        projectElement.workDisplaySize = workDisplaySize;
        return projectElement;
    }
    public static ProjectElement createWorkDetailElement(
            Project project,
            WorkDetail workDetail,
            WorkAlignment workDetailAlignment,
            WorkDisplaySize workDetailDisplaySize
    ){
        ProjectElement projectElement = new ProjectElement();
        projectElement.projectElementType = ProjectElementType.DETAIL;
        projectElement.project = project;
        projectElement.workDetail = workDetail;
        projectElement.workDetailAlignment = workDetailAlignment;
        projectElement.workDetailDisplaySize = workDetailDisplaySize;
        return projectElement;
    }
    public static ProjectElement createTextBoxElement(
            Project project,
            TextBox textBox,
            TextBoxAlignment textBoxAlignment
    ){
        ProjectElement projectElement = new ProjectElement();
        projectElement.projectElementType = ProjectElementType.TEXTBOX;
        projectElement.project = project;
        projectElement.textBox = textBox;
        projectElement.textBoxAlignment = textBoxAlignment;
        return projectElement;
    }

    public static ProjectElement createDocumentElement(
            Project project,
            Document document,
            WorkAlignment documentAlignment
    ){
        ProjectElement projectElement = new ProjectElement();
        projectElement.projectElementType = ProjectElementType.DOCUMENT;
        projectElement.project = project;
        projectElement.document = document;
        projectElement.documentAlignment = documentAlignment;
        return projectElement;
    }

    public static ProjectElement createDividerElement(
            Project project,
            DividerType dividerType
    ){
        ProjectElement projectElement = new ProjectElement();
        projectElement.projectElementType = ProjectElementType.DIVIDER;
        projectElement.project = project;
        projectElement.dividerType = dividerType;
        return projectElement;
    }
}
