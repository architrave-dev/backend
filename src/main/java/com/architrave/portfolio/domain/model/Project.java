package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "project_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    private List<ProjectInfo> projectInfoList;

    private String nation;
    private String city;
    private String address;

    private String supportedBy;

    private Integer projectOrder;
    private Boolean isDeleted;

}
