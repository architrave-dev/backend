package com.architrave.portfolio.api.dto.projectElement.request;

import com.architrave.portfolio.domain.model.enumType.DisplayAlignment;
import com.architrave.portfolio.domain.model.enumType.DisplaySize;
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
    private DisplayAlignment workDetailAlignment;
    private DisplaySize workDetailDisplaySize;
}
