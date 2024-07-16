package com.architrave.portfolio.api.dto.landingBox.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLandingBoxDto {
    private Long id;
    private String originUrl;
    private String thumbnailUrl;
    private String title;
    private String description;
    private Boolean isDeleted;
}
