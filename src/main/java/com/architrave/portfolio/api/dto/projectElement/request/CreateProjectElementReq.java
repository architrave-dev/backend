package com.architrave.portfolio.api.dto.projectElement.request;

import com.architrave.portfolio.api.dto.reorder.request.IndexDto;
import com.architrave.portfolio.api.dto.document.request.CreateDocumentReq;
import com.architrave.portfolio.api.dto.textBox.request.CreateTextBoxReq;
import com.architrave.portfolio.api.dto.work.request.CreateWorkDetailReq;
import com.architrave.portfolio.api.dto.work.request.CreateWorkReq;
import com.architrave.portfolio.domain.model.enumType.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectElementReq extends IndexDto {

    @NotNull
    private Long projectId;
    @NotNull
    private ProjectElementType projectElementType;

    private CreateWorkReq createWorkReq;
    private CreateWorkDetailReq createWorkDetailReq;
    private CreateTextBoxReq createTextBoxReq;
    private CreateDocumentReq createDocumentReq;
    private DividerType dividerType;

    private DisplayAlignment displayAlignment;
    private DisplaySize displaySize;
    private TextAlignment textAlignment;
}
