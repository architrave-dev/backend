package com.architrave.portfolio.api.dto.project.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectReq {
    @NotEmpty
    private String title;
    private String description;
    private String originUrl;
    private String thumbnailUrl;
}
