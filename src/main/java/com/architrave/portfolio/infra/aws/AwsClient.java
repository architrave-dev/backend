package com.architrave.portfolio.infra.aws;

import com.architrave.portfolio.global.aop.logTrace.Trace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsClient {

    @Value("${spring.aws.region}")
    private Region region;

    @Value("${spring.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.aws.credentials.secret-key}")
    private String secretKey;

    @Bean
    public S3Client s3Client(){
        return S3Client.builder()
                .region(region)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }
    @Bean
    public S3Presigner s3Presigner(){
        return S3Presigner.builder()
                .region(region)
                .credentialsProvider(
                        s3Client().serviceClientConfiguration().credentialsProvider()
                )
                .build();
    }
}