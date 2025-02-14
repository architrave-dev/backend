package com.architrave.portfolio.api.service;

import com.architrave.portfolio.api.dto.email.request.EmailReq;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;


@Trace
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final SesClient sesClient;

    public void sendEmail(EmailReq emailReq) {

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .destination(
                        Destination.builder()
                                .toAddresses(emailReq.getTo())
                                .build()
                )
                .message(
                        Message.builder()
                                .subject(Content.builder()
                                        .data(emailReq.getSubject())
                                        .build())
                                .body(Body.builder()
                                        .text(Content.builder()
                                                .data(emailReq.getBody())
                                                .build())
                                        .build())
                                .build()
                )
                .source(emailReq.getFrom()) // 검증된 이메일 주소(Verified) 또는 검증된 도메인의 주소
                .build();

        try {
            // 실제로 메일 전송
            SendEmailResponse response = sesClient.sendEmail(sendEmailRequest);
            System.out.println("메일 전송 완료. 메시지 ID: " + response.messageId());
        } catch (SesException e) {
            throw e;
        }
    }
}
