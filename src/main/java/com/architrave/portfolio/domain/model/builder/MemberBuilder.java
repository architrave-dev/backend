package com.architrave.portfolio.domain.model.builder;

import com.architrave.portfolio.domain.model.LandingBox;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemberBuilder {
    private String email;
    private String password;
    private String username;
    private RoleType role;
    private String description;
    private LandingBox loadingBox;

    public MemberBuilder email(String email) {
        this.email = email;
        return this;
    }

    public MemberBuilder password(String password) {
        this.password = password;
        return this;
    }
    public MemberBuilder username(String username) {
        this.username = username;
        return this;
    }
    public MemberBuilder role(RoleType role) {
        this.role = role;
        return this;
    }

    public MemberBuilder description(String description) {
        this.description = description;
        return this;
    }

    public MemberBuilder loadingBox(LandingBox loadingBox) {
        this.loadingBox = loadingBox;
        return this;
    }
    public Member build() {
        validateMember();
        return Member.createMember(
                this.email,
                this.password,
                this.username,
                this.role,
                this.description,
                this.loadingBox
        );
    }
    private void validateMember(){
        if(email == null || password == null || username == null || role == null){
            throw new IllegalArgumentException("required value is empty in MemberBuilder");
        }
    }

}
