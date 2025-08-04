package com.est.mungpe.security.config;

import com.est.mungpe.exception.ExceptionMessage;
import com.est.mungpe.exception.MemberNotFound;
import com.est.mungpe.member.domain.Member;
import com.est.mungpe.member.dto.CustomUserPrincipal;
import com.est.mungpe.member.repository.MemberRepository;
import com.est.mungpe.security.domain.RefreshToken;
import com.est.mungpe.security.dto.KeyPair;
import com.est.mungpe.security.repository.adapter.TokenRepositoryAdapter;
import com.est.mungpe.security.service.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
// 유저 차단 확인, 리프레쉬 토큰 확인 및 액세스토큰 생성
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final JwtConfiguration jwtConfiguration;


    @Value("${custom.frontend.redirect-uri}")
    private String baseUrl;


    // SuccessHandler을 조회하면 자동적으로 onAuthenticationSuccess 메소드를 조회 한다.
    // Spring Security는 인증이 성공하면 AuthenticationSuccessHandler 타입으로 등록된 객체를 찾는다.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 유저 정보를 가져온다.
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

        //member Id를 가져온다.
        Long memberId = principal.getId();

        //SecurityContext에 어떤 객체가 들어갔는지 디버깅용으로 확인하기 위해
        log.info("Principal set to SecurityContext: {}", ((CustomUserPrincipal) authentication.getPrincipal()).getClass().getName());

        // 쿠키 생존 시간에 대한 설정
        int accessCookieMaxAge = (int) (jwtConfiguration.getValidation().getAccess() / 1000);
        int refreshCookieMaxAge = (int) (jwtConfiguration.getValidation().getRefresh() / 1000);

        // 유저의 벤 여부를 확인한다.
        if (principal.isBlocked()) {
            log.warn("Blocked user attempted to log in: {}", principal.getId());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, ExceptionMessage.MEMBER_BLOCKED_ERROR);
            return;
        }

        //Refresh 토큰 조회
        RefreshToken findRefreshToken = jwtTokenProvider.validateRefreshToken(memberId);

        if (findRefreshToken == null) {
            // 회원 조회
            Member findMember = memberRepository.findById(memberId)
                    .orElseThrow(() -> new MemberNotFound(ExceptionMessage.MEMBER_NOT_FOUND));

            // Access,Refresh 토큰 생성 및 저장
            KeyPair keyPair = jwtTokenProvider.generateKeyPair(findMember);
            String accessToken = keyPair.getAccessToken();
            String refreshToken = keyPair.getRefreshToken();

            // 쿠키에 토큰 추가
            addCookie(response, "accessToken", accessToken, accessCookieMaxAge);
            addCookie(response, "refreshToken", refreshToken, refreshCookieMaxAge);

            log.info("accessToken = {}", accessToken);
            log.info("refreshToken = {}", refreshToken);
            log.info("유효한 Refresh Token 없음 Refresh,Access Token 발급");

        } else {
            // 토큰이 있다면 Access 토큰만 발급.
            String accessToken = jwtTokenProvider.issueAccessToken(principal.getId(), principal.getRole().name());
            String refreshToken = findRefreshToken.getRefreshToken();

            // 쿠키에 토큰 추가
            addCookie(response, "accessToken", accessToken, accessCookieMaxAge);
            addCookie(response, "refreshToken", refreshToken, refreshCookieMaxAge);
            log.info("기존 Refresh Token 유효 AccessToken만 발급");
        }

        // Success 후 리디렉션
        getRedirectStrategy().sendRedirect(request,response,baseUrl+"/main");

        log.info("succesHandler 성공");

    }


    private void addCookie(HttpServletResponse response,
                           String name,
                           String value,
                           int maxAgeSeconds) {

        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true); //나중에 true로 바꿔야지 https 에서만 전송 된다.
        cookie.setDomain("mungpedia.kr");
        response.addCookie(cookie);
    }

}
