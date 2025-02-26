package com.architrave.portfolio.domain.model.builder;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.enumType.MemberStatus;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemberBuilder {
    private String email;
    private String password;
    private String username;
    private RoleType role;
    private MemberStatus status;

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
    public MemberBuilder status(MemberStatus status) {
        this.status = status;
        return this;
    }

    public Member build() {
        validateMember();
        return Member.createMember(
                this.email,
                this.password,
                this.username,
                this.role,
                this.status

        );
    }
    private void validateMember(){
        if(email == null || password == null || username == null || role == null || status == null){
            throw new RequiredValueEmptyException("required value is empty in MemberBuilder");
        }
    }
}
