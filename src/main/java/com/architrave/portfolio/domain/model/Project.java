package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
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

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<ProjectElement> projectElementList = new ArrayList<>();

    @Embedded
    private Address address;

    private String supportedBy;

    private Integer projectOrder;
    private Boolean isDeleted;

    public static Project createProject(
            Member member,
            String title,
            String description
    ){
        Project project = new Project();
        project.member = member;
        project.title = title;
        project.description = description;
        project.isDeleted = false;
        return project;
    }

    //member 변경 불가 변수 막기
    private void setId(Long id){};
    // 이게 가능한지 확인해봐야해
    private void setMember(Member member){}
    private void setStartDate(LocalDateTime startDate){}
    private void setEndDate(LocalDateTime endDate){}

    public void setDate(LocalDateTime startDate, LocalDateTime endDate){
        if( startDate == null && endDate != null){
            throw new IllegalArgumentException("if endDate is not null, startDate required");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
