package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.Size;
import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.WorkDetail;
import lombok.Data;

import java.util.List;

@Data
public class WorkWithDetailDto {
    private Long id;
    private String originUrl;
    private String thumbnailUrl;
    private String title;
    private String description;
    private Size size;
    private String material;
    private Integer prodYear;

    private List<WorkDetailDto> workDetailDtoList;

    public WorkWithDetailDto(Work work, List<WorkDetailDto> workDetailList) {
        this.id = work.getId();
        UploadFile uploadFile = work.getUploadFile();
        this.originUrl = uploadFile.getOriginUrl();
        this.thumbnailUrl = uploadFile.getThumbnailUrl();
        this.title = work.getTitle();
        this.description = work.getDescription();
        this.size = work.getSize();
        this.material = work.getMaterial();
        this.prodYear = work.getProdYear();
        this.workDetailDtoList = workDetailList;
    }
}
