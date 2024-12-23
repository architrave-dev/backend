package com.architrave.portfolio.api.dto.uploadFile.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreSignedUrlResponse {
    private String preSignedUrl;
}
