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
public class MemberWithTokenDto {
    private Long id;
    private String email;
    private String username;
    private String aui;
    private RoleType role;

    private String refreshToken;
    public MemberWithTokenDto(Member member, String token) {
        id = member.getId();
        email = member.getEmail();
        username = member.getUsername();
        aui = member.getAui();
        role = member.getRole();
        refreshToken = token;

        log.info("created member  id: {}", member.getId());
        log.info("created member  email: {}", member.getEmail());
        log.info("created member  username: {}", member.getUsername());
        log.info("created member  aui: {}", member.getAui());
        log.info("created member  role: {}", member.getRole());
        log.info("created member  token: {}", token);

    }
}
