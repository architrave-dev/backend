package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.Size;
import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.Work;
import lombok.Data;

@Data
public class WorkDto {
    private Long id;
//    private MemberSimpleDto member; //mvp-2까지 문제 없을 경우 삭제
    private String originUrl;
    private String thumbnailUrl;
    private String title;
    private String description;
    private Size size;
    private String material;
    private Integer prodYear;

    public WorkDto(Work work) {
        this.id = work.getId();
        UploadFile uploadFile = work.getUploadFile();
        this.originUrl = uploadFile.getOriginUrl();
        this.thumbnailUrl = uploadFile.getThumbnailUrl();
        this.title = work.getTitle();
        this.description = work.getDescription();
        this.size = work.getSize();
        this.material = work.getMaterial();
        this.prodYear = work.getProdYear();
    }
}
