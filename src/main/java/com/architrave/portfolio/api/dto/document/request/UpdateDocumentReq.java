package com.architrave.portfolio.api.dto.document.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDocumentReq {
    @NotNull
    private Long id;
    private String originUrl;
    private String thumbnailUrl;
    private String description;
}
