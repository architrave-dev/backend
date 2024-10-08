package com.architrave.portfolio.api.dto.landingBox.response;

import com.architrave.portfolio.domain.model.LandingBox;
import com.architrave.portfolio.domain.model.UploadFile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LandingBoxDto {
    private Long id;
    private String originUrl;
    private String thumbnailUrl;
    private String title;
    private String description;
    private Boolean isVisible;

    public LandingBoxDto(LandingBox landingBox) {
        this.id = landingBox.getId();
        UploadFile uploadFile = landingBox.getUploadFile();
        this.originUrl = uploadFile.getOriginUrl();
        this.thumbnailUrl = uploadFile.getThumbnailUrl();
        this.title = landingBox.getTitle();
        this.description = landingBox.getDescription();
        this.isVisible = landingBox.getIsVisible();
    }
}
