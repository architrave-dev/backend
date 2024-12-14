package com.architrave.portfolio.api.dto.projectElement.request;

import com.architrave.portfolio.api.dto.work.request.UpdateWorkReq;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import com.architrave.portfolio.domain.model.enumType.WorkDisplaySize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectElementWithWorkReq {
    @NotNull
    private Long projectId;
    @NotNull
    private Long workId;
    private WorkAlignment workAlignment;
    private WorkDisplaySize workDisplaySize;
}
