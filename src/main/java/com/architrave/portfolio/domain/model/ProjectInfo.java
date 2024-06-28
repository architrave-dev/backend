package com.architrave.portfolio.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
public class ProjectInfo {

    @Id
    @GeneratedValue
    @Column(name = "project_info_id")
    private Long id;

    private String customName;
    private String customValue;

}
