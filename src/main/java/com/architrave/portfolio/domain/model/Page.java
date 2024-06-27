package com.architrave.portfolio.domain.model;

import com.architrave.portfolio.domain.model.enumType.DividerType;
import com.architrave.portfolio.domain.model.enumType.TextBoxAlignment;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import jakarta.persistence.*;

@Entity
public class Page {

    @Id
    @GeneratedValue
    @Column(name = "page_id")
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToOne
    @JoinColumn(name = "work_id")
    private Work work;

    @Enumerated
    private WorkAlignment workAlignment;

    @OneToOne
    @JoinColumn(name = "text_box_id")
    private TextBox textBox;

    @Enumerated
    private TextBoxAlignment textBoxAlignment;

    @Enumerated
    private DividerType dividerType;
    private Integer order;
    private Boolean isRepresentative;
    private Boolean isVisible;
    private Boolean isDeleted;

}
