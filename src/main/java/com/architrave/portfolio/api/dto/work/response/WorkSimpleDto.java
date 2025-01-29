package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.Work;
import lombok.Data;

@Data
public class WorkSimpleDto {
    private Long id;
    private String title;
    private String originUrl;

    public WorkSimpleDto(Work work) {
        this.id = work.getId();
        UploadFile uploadFile = work.getUploadFile();
        this.title = work.getTitle();
        this.originUrl = uploadFile.getOriginUrl();
    }
    public WorkSimpleDto(Long id, String title, String originUrl) {
        this.id = id;
        this.title = title;
        this.originUrl = originUrl;
    }
}
