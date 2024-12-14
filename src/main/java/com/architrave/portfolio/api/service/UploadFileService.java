package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.net.URI;

@Trace
@Service
@RequiredArgsConstructor
@Slf4j
public class UploadFileService {
    private final S3Client s3;

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
        try{
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();
            s3.deleteObject(deleteRequest);
            log.info("delete success: {}", keyName);
        }catch (S3Exception e){
            log.error("S3 file deletion failed: {}", keyName, e);
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    public void deleteUploadFile(UploadFile uploadFile) {
        if(!uploadFile.getOriginUrl().equals("")) deleteFile(uploadFile.getOriginUrl());
        if(!uploadFile.getThumbnailUrl().equals("")) deleteFile(uploadFile.getThumbnailUrl());
    }
}
