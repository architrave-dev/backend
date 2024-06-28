package com.architrave.portfolio.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Size {
    private Integer width;
    private Integer height;
    private Integer depth;
}
