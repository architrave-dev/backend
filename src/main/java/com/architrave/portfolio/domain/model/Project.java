package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Project extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "project_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "upload_file_id")
    private UploadFile uploadFile;
    private String title;
    private String description;
    @OneToMany(mappedBy = "project",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ProjectElement> projectElementList = new ArrayList<>();

    //ProjectElement의 순서를 _ 구분자로 관리
    private String peIndex;

    //ProjectInfo의 순서를 _ 구분자로 관리
    private String piIndex;

    public static Project createProject(
            Member member,
            UploadFile uploadFile,
            String title,
            String description
    ){
        Project project = new Project();
        project.member = member;
        project.uploadFile = uploadFile;
        project.title = title;
        project.description = description;
        return project;
    }

    //member 변경 불가 변수 막기
    private void setId(Long id){};
    // 이게 가능한지 확인해봐야해
    private void setMember(Member member){}

    /**
     * Project의 대표이미지 url을 설정한다.
     */
    public void setUploadFileUrl(String originUrl, String thumbnailUrl ){
        this.uploadFile.setImgUrls(originUrl, thumbnailUrl);
    }
}
