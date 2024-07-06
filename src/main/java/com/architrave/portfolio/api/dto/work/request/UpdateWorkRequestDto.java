package com.architrave.portfolio.api.dto.work.request;

import com.architrave.portfolio.domain.model.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateWorkRequestDto {
    private String title;
    private String description;
    private Size size;
    private String material;
    private Integer prodYear;
}
