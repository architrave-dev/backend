package com.architrave.portfolio.domain.model;

import com.architrave.portfolio.domain.model.enumType.MemberStatus;
import com.architrave.portfolio.domain.model.enumType.RoleType;
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

    public static Verification createVerification(
            String key,
            String code
    ){
        Verification verification = new Verification();
        verification.key = key;
        verification.code = code;

        return verification;
    }
}
