package com.architrave.portfolio.api.dto.document.response;

import com.architrave.portfolio.domain.model.Document;
import com.architrave.portfolio.domain.model.UploadFile;
import lombok.Data;

@Data
public class DocumentDto {
    private Long id;
    private UploadFile uploadFile;
    private String description;

    public DocumentDto(Document document){
        this.id = document.getId();
        this.uploadFile = document.getUploadFile();
        this.description = document.getDescription();
    }
}
