package com.architrave.portfolio.domain.model;

import com.architrave.portfolio.domain.model.enumType.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private String password;
    @Column(unique = true)
    @NotNull
    private String aui;

    @Enumerated
    @NotNull
    private RoleType role;

    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "landing_box_id")
    private LandingBox loadingBox;


}
