package com.architrave.portfolio.api.dto.project.request;

import com.architrave.portfolio.api.dto.reorder.request.IndexDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectReq extends IndexDto {
    @NotEmpty
    private String title;
    private String description;
    private String originUrl;
}
