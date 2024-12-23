package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.Work;
import lombok.Data;

@Data
public class WorkSimpleDto {
    private Long id;
    private String thumbnailUrl;
    private String title;

    public WorkSimpleDto(Work work) {
        this.id = work.getId();
        UploadFile uploadFile = work.getUploadFile();
        this.thumbnailUrl = uploadFile.getThumbnailUrl();
        this.title = work.getTitle();
    }
    public WorkSimpleDto(Long id, String title, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
    }
}
