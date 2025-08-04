package com.est.mungpe.logo.service.Impl;

import com.est.mungpe.exception.ExceptionMessage;
import com.est.mungpe.exception.LogoException;
import com.est.mungpe.image.domain.Image;
import com.est.mungpe.image.domain.ImageDomainType;
import com.est.mungpe.image.service.ImageConvertService;
import com.est.mungpe.image.service.ImageService;
import com.est.mungpe.image.service.S3StorageService;
import com.est.mungpe.logo.domain.Logo;
import com.est.mungpe.logo.dto.GetLogoResponse;
import com.est.mungpe.logo.dto.LogoResponse;
import com.est.mungpe.logo.reposotory.LogoRepository;
import com.est.mungpe.logo.service.LogoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LogoServiceImpl implements LogoService {

    private final LogoRepository logoRepository;
    private final ImageConvertService imageConvertService;
    private final ImageService imageService;
    private final S3StorageService s3StorageService;

    @Override
    @Transactional(readOnly = false)
    public LogoResponse createOrUpdateLogo(MultipartFile sourceImage) throws IOException {

        Optional<Logo> optionalLogo = logoRepository.findById(1l);

        //버퍼 이미지 타입 변환
        BufferedImage image = ImageIO.read(sourceImage.getInputStream());

        //바이트 파일로 변환
        byte[] fileData = imageConvertService.convertBufferedImageToByteArray(image);

        //유니크 파일 경로 만들기
        String fileName = UUID.randomUUID().toString();

        String message;

        if(optionalLogo.isEmpty()) {

            //s3 업로드
            String imageUrl = s3StorageService.upload(fileData,fileName);
            log.info("imageUrl = {}", imageUrl);

            //로고 저장
            Logo logo = Logo.builder()
                    .url(imageUrl)
                    .build();
            logoRepository.save(logo);

            message = "logo created";


        } else {

            Logo logo = optionalLogo.get();

            String key = logo.getUrl().replaceFirst("https://mungpedia.s3.ap-northeast-2.amazonaws.com/", "");

            s3StorageService.delete(key);;
            log.info("s3 기존 이미지 삭제 성공 key = {}", key);

            //s3 업로드
            String imageUrl = s3StorageService.upload(fileData,fileName);
            log.info("새로운 s3 이미지 업로드 imageUrl = {}", imageUrl);

            logo.update(imageUrl);

            message = "logo updated";

        }


        return LogoResponse.builder()
                .message(message)
                .result(true)
                .build();

    }

    @Override
    public GetLogoResponse getLogo() {
        Logo logo = logoRepository.findById(1l)
                .orElseThrow(() -> new LogoException(ExceptionMessage.LOGO_NOT_FOUND));

        return GetLogoResponse.builder()
                .logo(logo)
                .message("get Logo Success")
                .result(true)
                .build();
    }




}
