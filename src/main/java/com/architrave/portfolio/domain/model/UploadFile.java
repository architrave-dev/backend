package com.architrave.portfolio.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "upload_file_id")
    private Long id;

    //originUrl과 thumbnailUrl은 항상 함께 존재한다.
    private String originUrl;
    private String thumbnailUrl;

    /**
     * 서비스 로직에 직접 사용 x <br/>
     * 연관관계 메소드 사용 권장
     */
    public void setImgUrls(String originUrl, String thumbnailUrl){
        this.originUrl = originUrl;
        this.thumbnailUrl = thumbnailUrl;
    }
    /**
     * 서비스 로직에 직접 사용 x <br/>
     * 연관관계 메소드 사용 권장
     */
    public void removeImg(){
        this.originUrl = null;
        this.thumbnailUrl = null;
    }

}
