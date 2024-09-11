package com.architrave.portfolio.api.dto.projectElement.request;

import com.architrave.portfolio.api.dto.textBox.request.CreateTextBoxReq;
import com.architrave.portfolio.api.dto.work.request.CreateWorkReq;
import com.architrave.portfolio.domain.model.enumType.DividerType;
import com.architrave.portfolio.domain.model.enumType.ProjectElementType;
import com.architrave.portfolio.domain.model.enumType.TextBoxAlignment;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectElementReq {

    @NotNull
    private Long projectId;
    @NotNull
    private ProjectElementType projectElementType;

    //work 일 경우
    private CreateWorkReq createWorkReq;
    private WorkAlignment workAlignment;
    //textbox 일 경우
    private CreateTextBoxReq createTextBoxReq;
    private TextBoxAlignment textBoxAlignment;
    //divider일 경우
    private DividerType dividerType;
}
