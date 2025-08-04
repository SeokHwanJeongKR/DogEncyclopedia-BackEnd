package com.est.mungpe.image.service;

import java.io.IOException;

public interface S3StorageService {
    //필자는 서비스를 구현을 하여 사용하기에 override를 썼으나 단일 클래스로 사용시 @Override는 생략 가능하다.
    String upload(byte[] file, String fileName) throws IOException;

    void delete(String fileName);
}
