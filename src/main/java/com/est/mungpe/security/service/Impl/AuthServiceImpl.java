package com.est.mungpe.security.service.Impl;

import com.est.mungpe.exception.EmptyRefreshToken;
import com.est.mungpe.exception.ExceptionMessage;
import com.est.mungpe.exception.ExistingAuthenticationIsNull;
import com.est.mungpe.exception.InvalidRefreshTokenProvided;
import com.est.mungpe.security.config.JwtConfiguration;
import com.est.mungpe.security.domain.RefreshToken;
import com.est.mungpe.security.dto.LogoutResponse;
import com.est.mungpe.security.dto.ReissueTokenResponse;
import com.est.mungpe.security.dto.TokenBody;
import com.est.mungpe.security.repository.TokenRepository;
import com.est.mungpe.security.service.AuthService;
import com.est.mungpe.security.service.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtConfiguration jwtConfiguration;
    private final TokenRepository tokenRepository;

    // 새로운 액세스 토큰 발급
    @Override
    public ReissueTokenResponse reissueAccessToken(String refreshToken,Authentication authentication ,HttpServletRequest request, HttpServletResponse response) {


        log.info("authentication = {}", authentication);
        log.info("authentication.getPrincipal() = {}", authentication.getPrincipal());

        log.info("reissueAccessToken 액세스 토큰 재발급");
        // 리프레쉬 토큰 확인 문제가 있는지
        if (!jwtTokenProvider.validate(refreshToken)) {
            throw new InvalidRefreshTokenProvided(ExceptionMessage.INVALID_REFRESH_TOKEN_PROVIDED);
        }

        // 토큰을 조회 및
        Optional<RefreshToken> validRefTokenByToken = tokenRepository.findValidRefTokenByToken(refreshToken);

        if (validRefTokenByToken.isEmpty()) {
            throw new EmptyRefreshToken(ExceptionMessage.TOKEN_IS_EMPTY);
        }

        // 토큰 파싱
        TokenBody tokenBody = jwtTokenProvider.parseJwt(refreshToken);

        // 새 Access Token 발급
        String newAccessToken = jwtTokenProvider.issueAccessToken(tokenBody.getMemberId(), tokenBody.getRole());

        ReissueTokenResponse result = ReissueTokenResponse.builder()
                .accessToken(newAccessToken)
                .message("Access Token 발급 완료")
                .result(true)
                .build();

        int accessCookieMaxAge = (int) (jwtConfiguration.getValidation().getAccess() / 1000);


        Cookie cookie = new Cookie("accessToken", newAccessToken);
        cookie.setMaxAge(accessCookieMaxAge);
        cookie.setDomain("mungpedia.kr");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true); //나중에 true로 바꿔야지 https 에서만 전송 된다.
        response.addCookie(cookie);


        return result;
    }

    // 로그아웃
    // AccessToken은 어차피 DB 저장도 안하고 만료시간도 짧아서 처리 안해도 된다.
    @Override
    public LogoutResponse logout(String refreshToken, Authentication authentication, HttpServletResponse response) {

        log.info("authentication = {}", authentication);
        log.info("authentication.getPrincipal() = {}", authentication.getPrincipal());

        if (authentication == null) {
            throw new ExistingAuthenticationIsNull(ExceptionMessage.EXISTING_AUTHENTICATION_IS_NULL);
        }

        // Refresh 토큰만 DB 검증 후 블랙리스트에 추가
        Optional<RefreshToken> refreshTokenOptional = tokenRepository.findValidRefTokenByToken(refreshToken);
        if (refreshTokenOptional.isPresent()) {

            tokenRepository.appendBlackList(refreshTokenOptional.get());
            log.info("로그아웃 완료 (Refresh 토큰 블랙리스트 등록) refresh={}", refreshToken);
            LogoutResponse result = LogoutResponse.builder()
                    .message("로그아웃 완료(Refresh 토큰 블랙리스트 등록)")
                    .result(true)
                    .build();
            SecurityContextHolder.clearContext();

            Cookie cookie = new Cookie("accessToken", null);
            cookie.setMaxAge(0);
            cookie.setDomain("mungpedia.kr");
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true); //나중에 true로 바꿔야지 https 에서만 전송 된다.
            response.addCookie(cookie);

            return result;

        } else {

            // 이미 블랙리스트이거나, 존재하지 않는 토큰
            log.info("로그아웃 완료 (Refresh 토큰이 DB에 없거나 이미 블랙리스트에 있음) refresh={}", refreshToken);
            LogoutResponse result = LogoutResponse.builder()
                    .message("로그아웃 완료(Refresh 토큰이 DB에 없거나 이미 블랙리스트에 있음)")
                    .result(true)
                    .build();
            SecurityContextHolder.clearContext();

            Cookie cookie = new Cookie("accessToken", null);
            cookie.setMaxAge(0);
            cookie.setDomain("mungpedia.kr");
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true); //나중에 true로 바꿔야지 https 에서만 전송 된다.
            response.addCookie(cookie);

            return result;
        }

    }

}
