package com.architrave.portfolio.api.dto.memberInfo.response;

import com.architrave.portfolio.domain.model.MemberInfo;
import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.enumType.CountryType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberInfoDto {
    private Long id;
    private UploadFile uploadFile;
    private String name;
    private CountryType country;
    private Integer year;
    private String email;
    private String contact;
    private String description;

    public MemberInfoDto(MemberInfo memberInfo) {
        this.id = memberInfo.getId();
        this.uploadFile = memberInfo.getUploadFile();
        this.name = memberInfo.getName();
        this.country = memberInfo.getCountry();
        this.year = memberInfo.getYear();
        this.email = memberInfo.getEmail();
        this.contact = memberInfo.getContact();
        this.description = memberInfo.getDescription();
    }
}
