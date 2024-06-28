package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LandingBox extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "landing_box_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "upload_file_id")
    private UploadFile uploadFile;

    private String title;
    private String description;
    private Boolean isDeleted;
}
