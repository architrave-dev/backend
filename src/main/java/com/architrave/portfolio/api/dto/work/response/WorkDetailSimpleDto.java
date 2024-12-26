package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.WorkDetail;
import lombok.Data;

@Data
public class WorkDetailSimpleDto {
    private Long id;
    private String thumbnailUrl;

    public WorkDetailSimpleDto(WorkDetail workDetail) {
        this.id = workDetail.getId();
        UploadFile uploadFile = workDetail.getUploadFile();
        this.thumbnailUrl = uploadFile.getThumbnailUrl();
    }
    public WorkDetailSimpleDto(Long id, String thumbnailUrl) {
        this.id = id;
        this.thumbnailUrl = thumbnailUrl;
    }
}
