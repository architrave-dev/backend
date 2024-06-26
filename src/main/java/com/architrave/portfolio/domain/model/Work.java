package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Work extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "work_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

//    private UploadFile uploadFile;

    private String title;
    private String description;

    private Integer width;
    private Integer height;
    private Integer depth;

    private String material;
    private Integer prodYear;
    private Boolean isDeleted;

}
