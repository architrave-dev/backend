package com.architrave.portfolio.api.dto.document.request;

import com.architrave.portfolio.api.dto.uploadFile.request.UpdateUploadFileReq;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDocumentReq {
    @NotNull
    private Long id;
    private UpdateUploadFileReq updateUploadFileReq;
    private String description;
}
