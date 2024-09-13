package com.architrave.portfolio.api.dto.projectElement.request;

import com.architrave.portfolio.domain.model.enumType.DividerType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDividerProjectElementReq {
    @NotNull
    private Long id;
    @NotNull
    private Long projectId;
    private DividerType dividerType;
}
