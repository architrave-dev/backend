package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Verification {

    @Id @GeneratedValue
    @Column(name = "verification_id")
    private Long id;

    @Column(unique = true)
    @NotNull
    private String key;
    @NotNull
    private String code;
}
