package com.architrave.portfolio.api.dto.reorder.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReorderReq {
    private Integer index;
    private Long id;
}
