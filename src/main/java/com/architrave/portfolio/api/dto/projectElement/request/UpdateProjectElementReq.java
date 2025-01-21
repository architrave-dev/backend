package com.architrave.portfolio.api.dto.projectElement.request;

import com.architrave.portfolio.api.dto.document.request.UpdateDocumentReq;
import com.architrave.portfolio.api.dto.textBox.request.UpdateTextBoxReq;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkDetailReq;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkReq;
import com.architrave.portfolio.domain.model.enumType.DividerType;
import com.architrave.portfolio.domain.model.enumType.TextAlignment;
import com.architrave.portfolio.domain.model.enumType.DisplayAlignment;
import com.architrave.portfolio.domain.model.enumType.DisplaySize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectElementReq {
    private Long projectElementId;
    private UpdateWorkReq updateWorkReq;
    private UpdateWorkDetailReq updateWorkDetailReq;
    private UpdateTextBoxReq updateTextBoxReq;
    private UpdateDocumentReq updateDocumentReq;

    private DisplayAlignment displayAlignment;
    private DisplaySize displaySize;
    private TextAlignment textAlignment;
    private DividerType dividerType;
}
