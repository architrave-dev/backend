package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.WorkDetail;
import lombok.Data;

@Data
public class WorkDetailDto {
    private Long id;
    private String originUrl;
    private String thumbnailUrl;
    private String description;

    public WorkDetailDto(WorkDetail workDetail) {
        this.id = workDetail.getId();
        UploadFile uploadFile = workDetail.getUploadFile();
        this.originUrl = uploadFile.getOriginUrl();
        this.thumbnailUrl = uploadFile.getThumbnailUrl();
        this.description = workDetail.getDescription();
    }
}