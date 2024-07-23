package com.architrave.portfolio.api.dto.textBox.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTextBoxReq {
    private Long id;
    private String content;
    private Boolean isDeleted;
}
