package com.architrave.portfolio.api.dto.project.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectReq {
    private String title;
    private String description;
}
