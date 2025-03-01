package com.architrave.portfolio.api.dto.auth.response;

import com.architrave.portfolio.domain.model.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchDto {
    private String username;
    private String aui;

    public MemberSearchDto(Member member) {
        username = member.getUsername();
        aui = member.getAui();
    }
}
