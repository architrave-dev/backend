package com.architrave.portfolio.api.dto.projectElement.request;

import com.architrave.portfolio.domain.model.enumType.DividerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDividerProjectElementReq {
    private Long id;
    private Long projectId;
    private DividerType dividerType;
    private Integer peOrder;
}
