package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Size;
import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.Work;
import lombok.Builder;
import lombok.Data;

@Data
public class WorkResponse {
    private Long id;
    private Member member;
    private UploadFile uploadFile;
    private String title;
    private String description;
    private Size size;
    private String material;
    private Integer prodYear;

    @Builder
    public WorkResponse(Work work) {
        this.id = work.getId();
        this.member = work.getMember();
        this.uploadFile = work.getUploadFile();
        this.title = work.getTitle();
        this.description = work.getDescription();
        this.size = work.getSize();
        this.material = work.getMaterial();
        this.prodYear = work.getProdYear();
    }
}
