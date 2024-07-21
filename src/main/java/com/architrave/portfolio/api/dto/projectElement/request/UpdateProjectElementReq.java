package com.architrave.portfolio.api.dto.projectElement.request;

import com.architrave.portfolio.domain.model.enumType.DividerType;
import com.architrave.portfolio.domain.model.enumType.TextBoxAlignment;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectElementReq {
    private Long id;
    private Long projectId;
    private Long workId;
    private WorkAlignment workAlignment;
    private Long textBoxId;
    private TextBoxAlignment textBoxAlignment;
    private DividerType dividerType;
    private Integer peOrder;
    private Boolean isRepresentative;
}
