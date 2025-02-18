package com.architrave.portfolio.api.dto.email.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailReq {

    private String from;    // 발신 이메일
    private String to;      // 수신 이메일
    private String subject; // 제목
    private String body;    // 본문(텍스트)
}
