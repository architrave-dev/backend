package com.architrave.portfolio.api.dto.work.request;

import com.architrave.portfolio.domain.model.Size;
import com.architrave.portfolio.domain.model.enumType.WorkType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkReq {

    private WorkType workType;
    @NotEmpty @URL
    private String originUrl;
    @NotEmpty @URL
    private String thumbnailUrl;
    @NotEmpty
    private String title;
    private String description;
    private Size size;
    private String material;
    private Integer prodYear;
    private String price;
    private String collection;
}
