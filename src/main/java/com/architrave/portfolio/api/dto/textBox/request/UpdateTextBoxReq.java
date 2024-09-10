package com.architrave.portfolio.api.dto.textBox.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTextBoxReq {
    @NotNull
    private Long id;
    private String content;
}
