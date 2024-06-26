package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;

@Entity
public class TextBox extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "text_box_id")
    private Long id;
    private String content;
    private Boolean isDeleted;

}
