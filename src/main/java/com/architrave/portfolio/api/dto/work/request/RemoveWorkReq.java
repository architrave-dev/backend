package com.architrave.portfolio.api.dto.work.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RemoveWorkReq {
    @NotNull
    private Long id;
}
