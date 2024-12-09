package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.Size;
import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.enumType.WorkType;
import lombok.Data;

@Data
public class WorkDto {
    private Long id;
    private WorkType workType;
    private UploadFile uploadFile;
    private String title;
    private String description;
    private Size size;
    private String material;
    private Integer prodYear;
    private String price;
    private String collection;

    public WorkDto(Work work) {
        this.id = work.getId();
        this.workType = work.getWorkType();
        this.uploadFile = work.getUploadFile();
        this.title = work.getTitle();
        this.description = work.getDescription();
        this.size = work.getSize();
        this.material = work.getMaterial();
        this.prodYear = work.getProdYear();
        this.price = work.getPrice();
        this.collection = work.getCollection();
    }
}
