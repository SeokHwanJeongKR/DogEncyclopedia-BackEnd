package com.est.mungpe.image.service.impl;

import com.est.mungpe.image.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class S3StorageServiceImpl implements S3StorageService {

    //yml 에서 작성한 static을 가져왔다.
    @Value("${spring.cloud.aws.region.static}")
    private String region;


    //yml에서 사용한 버킷을 가져왔다.
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;


    //S3Client를 주입 받는다
    //이는 우리가 만든 클래스가 아니고 gradle을 추가하면 자동적으로 만들어지는 클래스이다.
    private final S3Client s3Client;


    //필자는 서비스를 구현을 하여 사용하기에 override를 썼으나 단일 클래스로 사용시 @Override는 생략 가능하다.
    @Override
    public String upload(byte[] file, String fileName) throws IOException {



        // s3에 실직적으로 넣어 주는 부분이다.
        // 버킷 이름, 적용할 파일이름, contentType를 PutObjectRequest타입으로 만들어
        //s3파일을 업로드 할 때 어디에,어떻게 업로드 할 지 설정하는 요청을 만든다.
        // RequestBody.fromBytes(file))에 실제로 얻르도 할 데이터를 넣어준다.
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType("image/jpeg")
                        .build(),
                RequestBody.fromBytes(file));

        //업로드가 완료된 파일의 S3 URL을 반환 해준다.
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
    }

    @Override
    public void delete(String fileName) {

        // s3의 파일을 지우는 요청을 만든다.
        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build()
        );
    }

}
