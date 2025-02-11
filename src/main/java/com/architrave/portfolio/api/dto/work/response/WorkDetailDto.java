package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.WorkDetail;
import lombok.Data;

@Data
public class WorkDetailDto {
    private Long id;
    private Long workId;
    private UploadFile uploadFile;
    private String description;

    public WorkDetailDto(WorkDetail workDetail) {
        this.id = workDetail.getId();
        this.workId = workDetail.getWork().getId();
        this.uploadFile = workDetail.getUploadFile();
        this.description = workDetail.getDescription();
    }
}
