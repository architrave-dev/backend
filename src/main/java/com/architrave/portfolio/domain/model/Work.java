package com.architrave.portfolio.domain.model;

import com.architrave.portfolio.domain.model.enumType.WorkType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


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

    @Enumerated(EnumType.STRING)
    private WorkType workType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "upload_file_id")
    private UploadFile uploadFile;
    private String title;
    private String description;
    @Embedded
    private Size size;
    private String material;
    private Integer prodYear;
    private String price;
    private String collection;

    public static Work createWork(
            Member member,
            WorkType workType,
            UploadFile uploadFile,
            String title,
            String description,
            Size size,
            String material,
            Integer prodYear,
            String price,
            String collection
    ){
        Work work = new Work();
        work.member = member;
        work.workType = workType;
        work.uploadFile = uploadFile;
        work.title = title;
        work.description = description;
        work.size = size;
        work.material = material;
        work.prodYear = prodYear;
        work.price = price;
        work.collection = collection;
        return work;
    }

    // ----- 연관관계 메소드 -----
    /**
     * Work의 이미지 url을 설정한다.
     */
    public void setUploadFileUrl(String originUrl, String thumbnailUrl ){
        this.uploadFile.setImgUrls(originUrl, thumbnailUrl);
    }
    /**
     * Work의 이미지 url을 null 처리한다. <br/>
     * 연결된 Work과의 관계를 끊지 않는다.
     */
    public void removeUploadFile(){
        this.uploadFile.removeImg();
    }
}
