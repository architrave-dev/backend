package com.architrave.portfolio.api.dto.projectElement.request;

import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import com.architrave.portfolio.domain.model.enumType.WorkDisplaySize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectElementWithWorkDetailReq {
    @NotNull
    private Long projectId;
    @NotNull
    private Long workDetailId;
    private WorkAlignment workDetailAlignment;
    private WorkDisplaySize workDetailDisplaySize;
}
