package com.architrave.portfolio.api.dto.work.request;

import com.architrave.portfolio.domain.model.enumType.WorkType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWorkDetailReq {

    @NotNull
    private Long workDetailId;
    private WorkType workType;
    private String originUrl;
    private String thumbnailUrl;
    private String description;
}
