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

    public void sendVerificationEmail(String email){
//        String verificationCode = String.format("%06d", (int)(Math.random() * 1000000));
        String verificationCode = "123456";
        String plainTextBody = "Thank you for signing up. Your verification code is: " +
                verificationCode +
                "\nIf you did not request this, please ignore this email.\n\n" +
                "Thank you,\nThe Architrave Team ☺️";

        String htmlBody =
                        "<html>" +
                        "  <head>" +
                        "    <meta charset=\"UTF-8\" />" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" +
                        "  </head>" +
                        "  <body style=\"font-family: Arial, sans-serif; color: Black;\">" +

                        "    <p style=\"font-size: 12px; line-height: 1.5;\">" +
                                "Thank you for signing up. Please enter the verification code below to complete your email verification." +
                        "    </p>" +
                        "    <p>" +
                        "      <strong style=\"font-size: 1.5em;\">" + verificationCode + "</strong>" +
                        "    </p>" +
                        "    <p style=\"font-size: 12px; line-height: 1.5;\">If you did not request this, please ignore this email.</p>" +
                        "    <p style=\"font-size: 12px; line-height: 1.5;\">Thank you,<br/>The Architrave Team</p>" +
                        "  </body>" +
                        "</html>";

        EmailReq emailReq = new EmailReq(
                "no-reply@architrive.com",
                email,
                "Verify Your Email",
                htmlBody
        );
        sendMIMEEmail(plainTextBody, emailReq);
    }

    public void sendMIMEEmail(String plainTextBody, EmailReq emailReq) {


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
                                        .charset("UTF-8")
                                        .build())
                                .body(Body.builder()
                                        .text(Content.builder()
                                                .data(plainTextBody)
                                                .charset("UTF-8")
                                                .build())
                                        .html(Content.builder()
                                                .data(emailReq.getBody()) // HTML content from EmailReq
                                                .charset("UTF-8")
                                                .build())
                                        .build())
                                .build()
                )
                .source(emailReq.getFrom()) // 검증된 이메일 주소(Verified) 또는 검증된 도메인의 주소
                .build();

        try {
            // 실제로 메일 전송
            SendEmailResponse response = sesClient.sendEmail(sendEmailRequest);
            System.out.println("MIME 메일 전송 완료. 메시지 ID: " + response.messageId());
        } catch (SesException e) {
            throw e;
        }
    }

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
