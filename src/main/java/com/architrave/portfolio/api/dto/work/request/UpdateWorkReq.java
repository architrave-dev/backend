package com.architrave.portfolio.api.dto.work.request;

import com.architrave.portfolio.domain.model.Size;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWorkReq {
    @NotNull
    private Long id;
    private String originImgUrl;
    private String thumbnailUrl;
    private String title;
    private String description;
    private Size size;
    private String material;
    private Integer prodYear;
    private Boolean isDeleted;
}
