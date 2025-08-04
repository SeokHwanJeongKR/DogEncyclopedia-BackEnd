package com.est.mungpe.logo.service;

import com.est.mungpe.logo.dto.GetLogoResponse;
import com.est.mungpe.logo.dto.LogoResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface LogoService {
    @Transactional(readOnly = false)
    LogoResponse createOrUpdateLogo(MultipartFile sourceImage) throws IOException;


    GetLogoResponse getLogo();
}
