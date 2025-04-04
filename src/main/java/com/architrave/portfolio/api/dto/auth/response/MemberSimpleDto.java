package com.architrave.portfolio.api.dto.auth.response;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSimpleDto {
    private Long id;
    private String email;
    private String username;
    private String aui;

    public MemberSimpleDto(Member member) {
        id = member.getId();
        email = member.getEmail();
        username = member.getUsername();
        aui = member.getAui();
    }
}
