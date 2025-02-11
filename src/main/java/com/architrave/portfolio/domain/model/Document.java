package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Document extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "document_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "upload_file_id")
    private UploadFile uploadFile;

    private String description;

    public static Document createDocument(UploadFile uploadFile,  String description){
        Document document  = new Document();
        document.uploadFile = uploadFile;
        document.description = description;
        return document;
    }

    // ----- 연관관계 메소드 -----
    /**
     * Work의 이미지 url을 설정한다.
     */
    public void setUploadFileUrl(String originUrl ){
        this.uploadFile.setImgUrls(originUrl);
    }
    /**
     * Work의 이미지 url을 null 처리한다. <br/>
     * 연결된 Work과의 관계를 끊지 않는다.
     */
    public void removeUploadFile(){
        this.uploadFile.removeImg();
    }
}
