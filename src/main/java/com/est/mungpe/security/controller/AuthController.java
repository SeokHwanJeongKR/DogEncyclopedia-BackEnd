package com.est.mungpe.security.controller;


import com.est.mungpe.security.dto.LogoutResponse;
import com.est.mungpe.security.dto.ReissueTokenResponse;
import com.est.mungpe.security.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<ReissueTokenResponse> reissueAccessToken( @CookieValue("refreshToken") String refreshToken,Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        log.info("Reissue access token 컨트롤러 도착");
        log.info("RefreshToken = {}" , refreshToken);
        ReissueTokenResponse result = authService.reissueAccessToken(refreshToken,authentication,request,response);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<LogoutResponse> logout( @CookieValue("refreshToken") String refreshToken, Authentication authentication,HttpServletResponse response) {
        log.info("log out 컨트롤러 도착");
        LogoutResponse result = authService.logout(refreshToken, authentication,response);

        return ResponseEntity.ok(result);
    }
}

