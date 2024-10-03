package com.architrave.portfolio.domain.model;

import com.architrave.portfolio.domain.model.enumType.CountryType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class MemberInfo extends BaseEntity{

    @Id @GeneratedValue
    @Column(name="member_info_id")
    private Long id;

    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "upload_file_id")
    private UploadFile uploadFile;

    private String name;

    @Enumerated(EnumType.STRING)
    private CountryType country;

    private Integer year;
    private String email;
    private String contact;
    private String description;


    public static MemberInfo createMemberInfo(
            Member member,
            UploadFile uploadFile,
            String name,
            CountryType country,
            Integer year,
            String email,
            String contact,
            String description
    ){
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.member = member;
        memberInfo.uploadFile = uploadFile;
        memberInfo.name = name;
        memberInfo.country = country;
        memberInfo.year = year;
        memberInfo.email = email;
        memberInfo.contact = contact;
        memberInfo.description = description;
        return memberInfo;
    }

    // ----- 연관관계 메소드 -----
    /**
     * UploadFile의 이미지 url을 설정한다.
     */
    public void setUploadFileUrl(String originUrl, String thumbnailUrl ){
        this.uploadFile.setImgUrls(originUrl, thumbnailUrl);
    }

    /**
     * UploadFile의 이미지 url을 null 처리한다. <br/>
     * 연결된 UploadFile과의 관계를 끊지 않는다.
     */
    public void removeUploadFile(){
        this.uploadFile.removeImg();
    }
}
