package com.architrave.portfolio.api.dto.projectElement.request;

import com.architrave.portfolio.api.dto.document.request.UpdateDocumentReq;
import com.architrave.portfolio.api.dto.textBox.request.UpdateTextBoxReq;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkDetailReq;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkReq;
import com.architrave.portfolio.domain.model.enumType.DividerType;
import com.architrave.portfolio.domain.model.enumType.TextBoxAlignment;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import com.architrave.portfolio.domain.model.enumType.WorkDisplaySize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectElementReq {
    private Long projectElementId;
    private UpdateWorkReq updateWorkReq;
    private WorkAlignment workAlignment;
    private WorkDisplaySize workDisplaySize;
    private UpdateWorkDetailReq updateWorkDetailReq;
    private WorkAlignment workDetailAlignment;
    private WorkDisplaySize workDetailDisplaySize;
    private UpdateTextBoxReq updateTextBoxReq;
    private TextBoxAlignment textBoxAlignment;
    private UpdateDocumentReq updateDocumentReq;
    private WorkAlignment documentAlignment;
    private DividerType dividerType;
}
