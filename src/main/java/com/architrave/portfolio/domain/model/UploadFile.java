package com.architrave.portfolio.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "upload_file_id")
    private String id;

    private String originUrl;
    private String thumbnailUrl;
    private Boolean isDeleted;
}
