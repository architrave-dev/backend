package com.architrave.portfolio.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class SocialMedia {
    private String twitter;
    private String instagram;
    private String facebook;
    private String youtube;
    private String url1;
}
