package com.architrave.portfolio.api.dto.billboard.response;

import com.architrave.portfolio.domain.model.Billboard;
import com.architrave.portfolio.domain.model.UploadFile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillboardDto {
    private Long id;
    private String originUrl;
    private String thumbnailUrl;
    private String title;
    private String description;
    private Boolean isVisible;

    public BillboardDto(Billboard billboard) {
        this.id = billboard.getId();
        UploadFile uploadFile = billboard.getUploadFile();
        this.originUrl = uploadFile.getOriginUrl();
        this.thumbnailUrl = uploadFile.getThumbnailUrl();
        this.title = billboard.getTitle();
        this.description = billboard.getDescription();
        this.isVisible = billboard.getIsVisible();
    }
}
