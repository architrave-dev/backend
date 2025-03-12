package com.architrave.portfolio.domain.model;

import com.architrave.portfolio.domain.model.enumType.MemberStatus;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity implements UserDetails {

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

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleType role;

    @Enumerated(EnumType.STRING)
    @NotNull
    private MemberStatus status;

    private String generateAui(){
        return UUID.randomUUID().toString().substring(0,8);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public static Member createMember(
            String email,
            String password,
            String username,
            RoleType role,
            MemberStatus status
    ){
        Member member = new Member();
        member.email = email;
        member.password = password;
        member.username = username;
        member.aui = member.generateAui();
        member.role = role;
        member.status = status;
        return member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRoleName()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    public void validateActiveStatus() {
        if (this.status == MemberStatus.INACTIVE) {
            throw new IllegalStateException("Member account is inactive: " + this.email);
        }
        if (this.status == MemberStatus.PENDING) {
            throw new IllegalStateException("Member account is pending approval: " + this.email);
        }
    }
}
