package com.architrave.portfolio.api.dto.textBox.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTextBoxReq {
    @NotEmpty
    private String content;
}
