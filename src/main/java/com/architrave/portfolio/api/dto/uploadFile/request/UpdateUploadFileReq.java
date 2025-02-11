package com.architrave.portfolio.api.dto.uploadFile.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUploadFileReq {
    private Long uploadFileId;
    private String originUrl;
}
