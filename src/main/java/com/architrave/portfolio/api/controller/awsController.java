package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.email.request.EmailReq;
import com.architrave.portfolio.api.dto.uploadFile.request.PreSignedUrlReq;
import com.architrave.portfolio.api.dto.uploadFile.response.PreSignedUrlResponse;
import com.architrave.portfolio.api.service.EmailService;
import com.architrave.portfolio.api.service.UploadFileService;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerCheck;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "00. AWS")  // => swagger 이름
@Trace
@RestController
@RequestMapping
@RequiredArgsConstructor
public class awsController {

    private final UploadFileService uploadFileService;
    private final EmailService emailService;

    @Operation(
            summary = "ALB의 health-check 처리",
            description = "ALB에서 target group에 health-check를 보낸다." +
                    "이에 200 응답을 리턴한다."
    )
    @GetMapping("/health-check")
    public ResponseEntity<Void> healthCheck(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "작가에게 이메일 보내기")
    @PostMapping("/send-email")
    public ResponseEntity<ResultDto<String>> sendEmail(
            @RequestParam("aui") String aui,
            @RequestBody EmailReq emailReq
    ){

        // 메일 주소 검증 로직 필요
        emailService.sendEmail(emailReq);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("이메일 전송 성공"));
    }

    @Operation(
            summary = "안전한 이미지업로드를 위해 pre-signed-url 요청",
            description = "프론트는 응답받은 pre-signed-url 로 이미지 업로드를 수행한다."
    )
    @PostMapping("/pre-signed-url")
    @OwnerCheck
    public ResponseEntity<ResultDto<PreSignedUrlResponse>> getPreSignedUrl(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @RequestBody PreSignedUrlReq preSignedUrlReq
    ){
        String generated = uploadFileService.generatePresignedUrl(
                preSignedUrlReq.getFileName(),
                preSignedUrlReq.getFileType()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new PreSignedUrlResponse(generated)));
    }
}
