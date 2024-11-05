package com.architrave.portfolio.api.dto.work.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWorkPropertyVisibleReq {
    @NotNull
    private Long workPropertyVisibleId;
    private Boolean workType;
    private Boolean imageUrl;
    private Boolean description;
    private Boolean price;
    private Boolean collection;
}
