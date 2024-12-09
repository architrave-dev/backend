package com.architrave.portfolio.api.dto.memberInfo.request;

import com.architrave.portfolio.api.dto.uploadFile.request.UpdateUploadFileReq;
import com.architrave.portfolio.domain.model.enumType.CountryType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberInfoReq {
    @NotNull
    private Long id;
    private UpdateUploadFileReq updateUploadFileReq;
    @NotNull
    private String name;
    private CountryType country;
    private Integer year;
    private String email;
    private String contact;
    private String description;
}
