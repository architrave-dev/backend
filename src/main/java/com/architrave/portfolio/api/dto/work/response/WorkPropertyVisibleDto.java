package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.WorkPropertyVisible;
import lombok.Data;

@Data
public class WorkPropertyVisibleDto {
    private Long workPropertyVisibleId;
    private Boolean workType;
    private Boolean imageUrl;
    private Boolean description;
    private Boolean price;
    private Boolean collection;

    public WorkPropertyVisibleDto(WorkPropertyVisible workPropertyVisible){
        this.workPropertyVisibleId = workPropertyVisible.getId();
        this.workType = workPropertyVisible.getWorkType();
        this.imageUrl = workPropertyVisible.getImageUrl();
        this.description = workPropertyVisible.getDescription();
        this.price = workPropertyVisible.getPrice();
        this.collection = workPropertyVisible.getCollection();
    }
}
