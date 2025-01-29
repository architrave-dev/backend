package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.WorkDetail;
import lombok.Data;

@Data
public class WorkDetailSimpleDto {
    private Long id;
    private String originUrl;

    public WorkDetailSimpleDto(WorkDetail workDetail) {
        this.id = workDetail.getId();
        UploadFile uploadFile = workDetail.getUploadFile();
        this.originUrl = uploadFile.getOriginUrl();
    }
    public WorkDetailSimpleDto(Long id, String originUrl) {
        this.id = id;
        this.originUrl = originUrl;
    }
}
