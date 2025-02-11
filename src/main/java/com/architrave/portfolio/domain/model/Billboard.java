package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Billboard extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "billboard_id")
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


    public static Billboard createBillboard(
            Member member,
            UploadFile uploadFile,
            String title,
            String description
    ) {
        Billboard billboard = new Billboard();
        billboard.member = member;
        billboard.uploadFile = uploadFile;
        billboard.title = title;
        billboard.description = description;
        billboard.isVisible = true;
        return billboard;
    }

    // ----- 연관관계 메소드 -----
    /**
     * UploadFile의 이미지 url을 설정한다. <br/>
     * Billboard의 UploadFile과 isVisible은 전혀 관계가 없다.
     */
    public void setUploadFileUrl(String originUrl ){
        this.uploadFile.setImgUrls(originUrl);
    }

    /**
     * UploadFile의 이미지 url을 null 처리한다. <br/>
     * 연결된 UploadFile과의 관계를 끊지 않는다. <br/>
     * Billboard의 UploadFile과 isVisible은 전혀 관계가 없다.
     */
    public void removeUploadFile(){
        this.uploadFile.removeImg();
    }
}
