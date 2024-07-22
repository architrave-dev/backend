package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Work extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "work_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "upload_file_id")
    private UploadFile uploadFile;
    private String title;
    private String description;
    @Embedded
    private Size size;
    private String material;
    private Integer prodYear;
    private Boolean isDeleted = false;

    public static Work createWork(
            Member member,
            UploadFile uploadFile,
            String title,
            String description,
            Size size,
            String material,
            Integer prodYear
    ){
        Work work = new Work();
        work.member = member;
        work.title = title;
        work.description = description;
        work.size = size;
        work.material = material;
        work.prodYear = prodYear;
        work.isDeleted = false;
        return work;
    }
}
