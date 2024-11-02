package com.architrave.portfolio.api.dto.work.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkDetailReq {

    @NotNull
    private Long workId;
    @NotEmpty @URL
    private String originUrl;
    @NotEmpty @URL
    private String thumbnailUrl;
    private String description;

}
