package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.api.dto.auth.response.MemberSimpleDto;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Size;
import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.Work;
import lombok.Data;

@Data
public class WorkDto {
    private Long id;
    private MemberSimpleDto member;
    private String originImgUrl;
    private String thumbnailUrl;
    private String title;
    private String description;
    private Size size;
    private String material;
    private Integer prodYear;
    private Boolean isDeleted;

    public WorkDto(Work work) {
        this.id = work.getId();
        this.member = new MemberSimpleDto(work.getMember());
        UploadFile uploadFile = work.getUploadFile();
        this.originImgUrl = uploadFile.getOriginUrl();
        this.thumbnailUrl = uploadFile.getThumbnailUrl();
        this.title = work.getTitle();
        this.description = work.getDescription();
        this.size = work.getSize();
        this.material = work.getMaterial();
        this.prodYear = work.getProdYear();
        this.isDeleted = work.getIsDeleted();
    }
}
