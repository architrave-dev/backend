package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
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
    private Boolean isVisible;


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
        landingBox.isVisible = true;
        return landingBox;
    }

    // ----- 연관관계 메소드 -----
    /**
     * UploadFile의 이미지 url을 설정한다. <br/>
     * LandingBox의 UploadFile과 isVisible은 전혀 관계가 없다.
     */
    public void setUploadFileUrl(String originUrl, String thumbnailUrl ){
        this.uploadFile.setImgUrls(originUrl, thumbnailUrl);
    }

    /**
     * UploadFile의 이미지 url을 null 처리한다. <br/>
     * 연결된 UploadFile과의 관계를 끊지 않는다. <br/>
     * LandingBox의 UploadFile과 isVisible은 전혀 관계가 없다.
     */
    public void removeUploadFile(){
        this.uploadFile.removeImg();
    }
}
