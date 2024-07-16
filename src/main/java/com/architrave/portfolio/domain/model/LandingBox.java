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

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "upload_file_id")
    private UploadFile uploadFile;

    private String title;
    private String description;
    private Boolean isDeleted;


    public static LandingBox createLandingBox(
            Member member,
            UploadFile uploadFile,
            String title,
            String description
    ) {
        LandingBox landingBox = new LandingBox();
        landingBox.member = member;
        landingBox.uploadFile = uploadFile;
        landingBox.title = title;
        landingBox.description = description;
        landingBox.isDeleted = false;
        return landingBox;
    }

    // ----- 연관관계 메소드 -----
    /**
     * UploadFile의 이미지 url을 설정한다. <br/>
     * LandingBox의 isDeleted를 false로 설정한다.
     */
    public void setUploadFileUrl(String originUrl, String thumbnailUrl ){
        this.uploadFile.setImgUrls(originUrl, thumbnailUrl);
        if(this.isDeleted) this.isDeleted = false;
    }

    /**
     * UploadFile의 이미지 url을 null 처리한다. <br/>
     * 연결된 UploadFile과의 관계를 끊지 않는다. <br/>
     * UploadFile의 이미지 url이 null 처리되면 LandingBox의 isDeleted는 true로 변한다.
     */
    public void removeUploadFile(){
        this.uploadFile.removeImg();
        this.isDeleted = true;
    }
}
