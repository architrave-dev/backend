package com.architrave.portfolio.api.dto.document.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocumentReq {

    private String originUrl;
    private String thumbnailUrl;
    private String description;
}
