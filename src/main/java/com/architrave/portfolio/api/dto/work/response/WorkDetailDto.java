package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.WorkDetail;
import com.architrave.portfolio.domain.model.enumType.WorkType;
import lombok.Data;

@Data
public class WorkDetailDto {
    private Long id;
    private WorkType workType;
    private UploadFile uploadFile;
    private String description;

    public WorkDetailDto(WorkDetail workDetail) {
        this.id = workDetail.getId();
        this.workType = workDetail.getWorkType();
        this.uploadFile = workDetail.getUploadFile();
        this.description = workDetail.getDescription();
    }
}
