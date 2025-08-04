package com.est.mungpe.logo.controller;


import com.est.mungpe.logo.dto.GetLogoResponse;
import com.est.mungpe.logo.dto.LogoResponse;
import com.est.mungpe.logo.service.LogoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("api/logo")
@RequiredArgsConstructor
public class LogoController {

    private final LogoService logoService;

    @GetMapping
    public ResponseEntity<GetLogoResponse> getLogo() {

        log.info("getLogo 컨트롤러 도착");

        GetLogoResponse result = logoService.getLogo();

        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<LogoResponse> createOrUpdateLogo(@RequestPart("image") MultipartFile file) throws IOException {

        log.info("create or update Logo 컨트롤러 도착");

        LogoResponse result = logoService.createOrUpdateLogo(file);

        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }



}
