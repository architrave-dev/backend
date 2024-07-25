package com.architrave.portfolio.api.dto.projectElement.request;

import com.architrave.portfolio.api.dto.work.request.UpdateWorkReq;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWorkProjectElementReq {
    private Long id;
    private Long projectId;
    private UpdateWorkReq updateWorkReq;
    private WorkAlignment workAlignment;
    private Integer peOrder;
}
