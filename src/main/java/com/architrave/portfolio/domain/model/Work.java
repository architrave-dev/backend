package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
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
        work.uploadFile = uploadFile;
        work.title = title;
        work.description = description;
        work.size = size;
        work.material = material;
        work.prodYear = prodYear;
        work.isDeleted = false;
        return work;
    }

    // ----- 연관관계 메소드 -----
    /**
     * Work의 이미지 url을 설정한다. <br/>
     * Work의 isDeleted를 false로 설정한다.
     */
    public void setUploadFileUrl(String originUrl, String thumbnailUrl ){
        this.uploadFile.setImgUrls(originUrl, thumbnailUrl);
        if(this.isDeleted) this.isDeleted = false;
    }
    /**
     * Work의 이미지 url을 null 처리한다. <br/>
     * 연결된 Work과의 관계를 끊지 않는다. <br/>
     * Work의 이미지 url이 null 처리되면 Work의 isDeleted는 true로 변한다.
     */
    public void removeUploadFile(){
        this.uploadFile.removeImg();
        this.isDeleted = true;
    }
}
