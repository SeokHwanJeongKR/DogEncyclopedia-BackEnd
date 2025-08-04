package com.est.mungpe.security.service;

import com.est.mungpe.security.dto.LogoutResponse;
import com.est.mungpe.security.dto.ReissueTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

    // 새로운 액세스 토큰 발급
    ReissueTokenResponse reissueAccessToken(String refreshToken,Authentication authentication, HttpServletRequest request, HttpServletResponse response);


    // 로그아웃
    // AccessToken은 어차피 DB 저장도 안하고 만료시간도 짧아서 처리 안해도 된다.
    LogoutResponse logout(String refreshToken, Authentication authentication, HttpServletResponse response);
}
