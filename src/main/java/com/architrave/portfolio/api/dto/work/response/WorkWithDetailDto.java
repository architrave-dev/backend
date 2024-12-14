package com.architrave.portfolio.api.dto.work.response;

import com.architrave.portfolio.domain.model.Work;
import lombok.Getter;

import java.util.List;

@Getter
public class WorkWithDetailDto extends WorkDto {
    private List<WorkDetailDto> workDetailList;

    public WorkWithDetailDto(Work work, List<WorkDetailDto> workDetailList) {
        super(work);
        this.workDetailList = workDetailList;
    }
}
