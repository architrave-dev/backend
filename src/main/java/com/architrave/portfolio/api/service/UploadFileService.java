package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Trace
@Service
@RequiredArgsConstructor
@Slf4j
public class UploadFileService {

    private final S3Client s3;
    private final LambdaClient lambda;
    private final S3Presigner preSigner;

    @Value("${spring.aws.s3.bucket-name}")
    private String bucketName;

    private String extractFileKeyFromUrl (String fileUrl){
        URI uri = URI.create(fileUrl);
        String fileKey = uri.getPath().substring(1);
        return fileKey;
    }

    //AWS S3 이미지 삭제
    private void deleteFile(String fileUrl){
        String keyName = extractFileKeyFromUrl(fileUrl);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("bucket", bucketName);
        payloadMap.put("key", keyName);

        ObjectMapper objectMapper = new ObjectMapper();
        String payload;
        try {
            payload = objectMapper.writeValueAsString(payloadMap);
        } catch (JsonProcessingException e) {
            System.err.println("Error creating JSON payload for Lambda invocation");
            e.printStackTrace();
            return;
        }

        // imageClean Lambda 함수 호출 요청 생성 (비동기 호출)
        InvokeRequest invokeRequest = InvokeRequest.builder()
                .functionName("imageClean")
                .invocationType(InvocationType.EVENT)
                .payload(SdkBytes.fromUtf8String(payload))
                .build();

        try {
            lambda.invoke(invokeRequest);
        } catch (Exception e) {
            System.err.println("Error invoking imageClean Lambda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteUploadFile(UploadFile uploadFile) {
        if(!uploadFile.getOriginUrl().equals("")) deleteFile(uploadFile.getOriginUrl());
    }

    public String generatePresignedUrl(String fileName, String fileType) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(fileType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5)) // URL 유효기간: 5분
                .putObjectRequest(objectRequest)
                .build();

        String preSignedUrl = preSigner.presignPutObject(presignRequest).url().toString();
        System.out.println("preSignedUrl: "+ preSignedUrl);

        return preSignedUrl;
    }
}
