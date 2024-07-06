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

    public void update(String title, String description, Size size, String material, Integer prodYear) {
        this.title = title;
        this.description = description;
        this.size = size;
        this.material = material;
        this.prodYear = prodYear;
    }
}
