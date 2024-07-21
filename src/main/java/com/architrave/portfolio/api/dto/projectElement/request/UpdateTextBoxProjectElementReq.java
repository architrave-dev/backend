package com.architrave.portfolio.api.dto.projectElement.request;

import com.architrave.portfolio.api.dto.textBox.request.UpdateTextBoxReq;
import com.architrave.portfolio.domain.model.enumType.TextBoxAlignment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTextBoxProjectElementReq {
    private Long id;
    private Long projectId;
    private UpdateTextBoxReq updateTextBoxReq;
    private TextBoxAlignment textBoxAlignment;
    private Integer peOrder;
}
