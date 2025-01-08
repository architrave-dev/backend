package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuVisible {
    private Boolean projects;
    private Boolean works;
    private Boolean about;
    private Boolean contact;
}