package com.est.mungpe.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public interface ImageConvertService {

    int MIN_WIDTH = 100;
    int MIN_HEIGHT = 100;

    int MAX_WIDTH = 5000;
    int MAX_HEIGHT = 5000;

    int MAX_SIZE = 30 * 1024 * 1024;

    List<byte[]> convert(List<MultipartFile> files);

    byte[] convertBufferedImageToByteArray(BufferedImage image) throws IOException;
}
