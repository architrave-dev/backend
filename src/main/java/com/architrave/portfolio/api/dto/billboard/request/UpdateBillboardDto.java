package com.architrave.portfolio.api.dto.billboard.request;

import com.architrave.portfolio.api.dto.uploadFile.request.UpdateUploadFileReq;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBillboardDto {
    @NotNull
    private Long id;
    private UpdateUploadFileReq updateUploadFileReq;
    private String title;
    private String description;
    private Boolean isVisible;
}
