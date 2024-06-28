package com.architrave.portfolio.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {
    private String nation;
    private String city;
    private String address;
}
