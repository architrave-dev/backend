package com.architrave.portfolio.api.dto.billboard.response;

import com.architrave.portfolio.domain.model.Billboard;
import com.architrave.portfolio.domain.model.UploadFile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillboardDto {
    private Long id;
    private UploadFile uploadFile;
    private String title;
    private String description;
    private Boolean isVisible;

    public BillboardDto(Billboard billboard) {
        this.id = billboard.getId();
        this.uploadFile = billboard.getUploadFile();
        this.title = billboard.getTitle();
        this.description = billboard.getDescription();
        this.isVisible = billboard.getIsVisible();
    }
}
