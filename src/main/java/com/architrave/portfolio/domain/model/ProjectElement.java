package com.architrave.portfolio.domain.model;

import com.architrave.portfolio.domain.model.enumType.*;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(
        name = "project_element",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"project_id", "work_id"}),
                @UniqueConstraint(columnNames = {"project_id", "work_detail_id"}),
        }
)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id")
    private Work work;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_detail_id")
    private WorkDetail workDetail;

    @OneToOne
    @JoinColumn(name = "text_box_id")
    private TextBox textBox;

    @OneToOne
    @JoinColumn(name = "document_id")
    private Document document;

    private Integer index;

    @Enumerated(EnumType.STRING)
    private DisplayAlignment displayAlignment;
    @Enumerated(EnumType.STRING)
    private DisplaySize displaySize;
    @Enumerated(EnumType.STRING)
    private TextAlignment textAlignment;

    @Enumerated(EnumType.STRING)
    private DividerType dividerType;


    public static ProjectElement createWorkElement(
            Project project,
            Work work,
            DisplayAlignment displayAlignment,
            DisplaySize displaySize
    ){
        ProjectElement projectElement = new ProjectElement();
        projectElement.projectElementType = ProjectElementType.WORK;
        projectElement.project = project;
        projectElement.work = work;
        projectElement.displayAlignment = displayAlignment;
        projectElement.displaySize = displaySize;
        return projectElement;
    }
    public static ProjectElement createWorkDetailElement(
            Project project,
            WorkDetail workDetail,
            DisplayAlignment displayAlignment,
            DisplaySize displaySize
    ){
        ProjectElement projectElement = new ProjectElement();
        projectElement.projectElementType = ProjectElementType.DETAIL;
        projectElement.project = project;
        projectElement.workDetail = workDetail;
        projectElement.displayAlignment = displayAlignment;
        projectElement.displaySize = displaySize;
        return projectElement;
    }
    public static ProjectElement createTextBoxElement(
            Project project,
            TextBox textBox,
            TextAlignment textAlignment
    ){
        ProjectElement projectElement = new ProjectElement();
        projectElement.projectElementType = ProjectElementType.TEXTBOX;
        projectElement.project = project;
        projectElement.textBox = textBox;
        projectElement.textAlignment = textAlignment;
        return projectElement;
    }

    public static ProjectElement createDocumentElement(
            Project project,
            Document document,
            DisplayAlignment displayAlignment
    ){
        ProjectElement projectElement = new ProjectElement();
        projectElement.projectElementType = ProjectElementType.DOCUMENT;
        projectElement.project = project;
        projectElement.document = document;
        projectElement.displayAlignment = displayAlignment;
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
