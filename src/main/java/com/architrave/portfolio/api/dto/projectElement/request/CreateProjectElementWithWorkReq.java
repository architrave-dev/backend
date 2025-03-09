package com.architrave.portfolio.api.dto.projectElement.request;

import com.architrave.portfolio.api.dto.reorder.request.IndexDto;
import com.architrave.portfolio.domain.model.enumType.DisplayAlignment;
import com.architrave.portfolio.domain.model.enumType.DisplaySize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectElementWithWorkReq extends IndexDto {
    @NotNull
    private Long projectId;
    @NotNull
    private Long workId;
    private DisplayAlignment displayAlignment;
    private DisplaySize displaySize;
}
