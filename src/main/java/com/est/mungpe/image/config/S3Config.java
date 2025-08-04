package com.est.mungpe.image.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${spring.cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secretKey}")
    private String secretKey;


    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                // 지역 설정
                .region(Region.of(region))
                // 자격 증명자 설정
                .credentialsProvider(StaticCredentialsProvider.create(
                        // accessKey와 secretKey로 자격 증명 생성
                        AwsBasicCredentials.create(accessKey,secretKey)
                ))
                .build();
    }
}
