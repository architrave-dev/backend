package com.architrave.portfolio.domain.model;

import com.architrave.portfolio.domain.model.enumType.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String username;    //활동명 (중복가능)
    @Column(unique = true)
    @NotNull
    private String aui;

    @Enumerated
    @NotNull
    private RoleType role;

    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "landing_box_id")
    private LandingBox landingBox;

    private String generateAui(String username){
        String uuid_8 = UUID.randomUUID().toString().substring(0,8);
        return username + "-" + uuid_8;
    }

    public void setUsername(String username) {
        this.username = username;
        this.aui = generateAui(username);
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Member createMember(
            String email,
            String password,
            String username,
            RoleType role,
            String description,
            LandingBox landingBox
    ){
        Member member = new Member();
        member.email = email;
        member.password = password;
        member.username = username;
        member.aui = member.generateAui(username);
        member.role = role;
        member.description = description;
        member.landingBox = landingBox;
        return member;
    }
}
