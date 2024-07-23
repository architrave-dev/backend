package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextBox extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "text_box_id")
    private Long id;
    private String content;
//    private Member member;
    private Boolean isDeleted;

    public static TextBox createTextBox(String content){
        TextBox textBox  = new TextBox();
        textBox.setContent(content);
        textBox.isDeleted = false;
        return textBox;
    }
}
