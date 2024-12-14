package com.architrave.portfolio.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Size {
    private String width;
    private String height;
    private String depth;
}
