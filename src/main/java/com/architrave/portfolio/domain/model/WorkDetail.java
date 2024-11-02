package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class WorkDetail {

    @Id
    @GeneratedValue
    @Column(name = "work_detail_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "work_id")
    private Work work;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "upload_file_id")
    private UploadFile uploadFile;
    private String description;


    public static WorkDetail createWorkDetail(
            Work work,
            UploadFile uploadFile,
            String description
    ){
        WorkDetail workDetail = new WorkDetail();
        workDetail.work = work;
        workDetail.uploadFile = uploadFile;
        workDetail.description = description;
        return workDetail;
    }

    // ----- 연관관계 메소드 -----
    /**
     * WorkDetail의 이미지 url을 설정한다.
     */
    public void setUploadFileUrl(String originUrl, String thumbnailUrl ){
        this.uploadFile.setImgUrls(originUrl, thumbnailUrl);
    }
    /**
     * WorkDetail의 이미지 url을 null 처리한다. <br/>
     * 연결된 Work과의 관계를 끊지 않는다.
     */
    public void removeUploadFile(){
        this.uploadFile.removeImg();
    }
}
