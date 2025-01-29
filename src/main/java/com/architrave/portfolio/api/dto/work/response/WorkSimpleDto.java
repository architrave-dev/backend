package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.Work;
import lombok.Data;

@Data
public class WorkSimpleDto {
    private Long id;
    private String title;

    public WorkSimpleDto(Work work) {
        this.id = work.getId();
        UploadFile uploadFile = work.getUploadFile();
        this.title = work.getTitle();
    }
    public WorkSimpleDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
