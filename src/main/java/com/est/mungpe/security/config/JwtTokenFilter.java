package com.est.mungpe.security.config;

import com.est.mungpe.exception.ExceptionMessage;
import com.est.mungpe.exception.MemberNotFound;
import com.est.mungpe.exception.RefreshTokenNotFound;
import com.est.mungpe.member.domain.Member;
import com.est.mungpe.member.dto.CustomUserPrincipal;
import com.est.mungpe.member.repository.MemberRepository;
import com.est.mungpe.security.domain.RefreshToken;
import com.est.mungpe.security.domain.RefreshTokenBlackList;
import com.est.mungpe.security.dto.TokenBody;
import com.est.mungpe.security.repository.RefreshTokenBlackListRepository;
import com.est.mungpe.security.repository.RefreshTokenRepository;
import com.est.mungpe.security.repository.TokenRepository;
import com.est.mungpe.security.service.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
//OncePerRequestFilter는 매 http요청 마다 한번만 실행되는 필터
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenRepository tokenRepository;


    // jwt 인증의 핵심 메소드 이며 토큰 추출, 유효성 검사, 사용자 정보 추출, DB에서 사용자 조회,SecurityContext에 등록할 인증 객체 생성 및 인증 객체 설정을 해야한다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("jwt 필터 도착");
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (uri.startsWith("/chat") || uri.startsWith("/topic")) {
            log.info("WebSocket 연결 요청, JWT 필터 스킵: {}", uri);
            filterChain.doFilter(request, response);
            return;
        }

        if (
                uri.startsWith("/oauth2") ||
                uri.startsWith("/login") ||
                uri.equals("/api/auth/reissue") ||
                        (method.equals("GET") && (
                                uri.startsWith("/api/logo") ||
                                        uri.startsWith("/api/search") ||
                                        uri.startsWith("/api/pedia") ||
                                        uri.startsWith("/api/event") ||
                                        uri.startsWith("/api/board") ||
                                        uri.startsWith("/api/comment")
                        ))
        ) {
            log.info("인증 필요 없는 요청, JWT 필터 생략");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 추출
        String realToken = resolveToken(request);



        // 토큰이 null 이거나 유효성 검사 실패 시  401Error
        if (realToken == null || !jwtTokenProvider.validate(realToken)) {
            refreshTokenRepository.findByRefreshToken(realToken)
                    .ifPresent(refreshToken -> {
                        tokenRepository.appendBlackList(refreshToken);
                    });
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token invalid or expired");
            return;
        }

        // 토큰도 문제 없고 유효성 검사도 성공시
        if (realToken != null && jwtTokenProvider.validate(realToken)) {
            // 사용자 정보 추출
            TokenBody tokenBody = jwtTokenProvider.parseJwt(realToken);
            // DB에서 사용자 조회
            Member member = memberRepository.findById(tokenBody.getMemberId())
                    .orElseThrow(() -> new MemberNotFound(ExceptionMessage.MEMBER_NOT_FOUND));

            //SecurityContext에 등록할 인증 객체 생성
            //Spring Security의 인증 처리 규칙에 따라, SecurityContext에는 반드시 UserDetails 또는 OAuth2User를 구현한 인증된 사용자 객체가 들어가야한다.
            //attributes란?	OAuth2 로그인 시, 제공자로부터 받은 사용자 정보 (JSON)
            //JWT 기반 인증에서는 클라이언트(브라우저 등)에서 이미 인증이 끝난 후, JWT만 주고받기 때문에 attributes는 필요 없다.
            CustomUserPrincipal customUserPrincipal = CustomUserPrincipal.from(member,null);
            log.info("customUserPrincipal.getId() = {}", customUserPrincipal.getId());

            // Spring Security는 JWT 내부 정보를 자동으로 인식하지 못하기 때문에 파싱한 사용자 정보 및 권한을 직접 Authentication 객체에 담아서 알려줘야 한다.
            // SecurityContext에 인증 객체 설정 (유저 정보, jwt토큰, 권한)
            Authentication authentication = new UsernamePasswordAuthenticationToken(customUserPrincipal, realToken, customUserPrincipal.getAuthorities());
            log.info("authentication.getPrincipal() = {}", ((CustomUserPrincipal) authentication.getPrincipal()).getEmail());
            log.info("authentication.getPrincipal() = {}", ((CustomUserPrincipal) authentication.getPrincipal()).getId());


            // SecurityContextHolder에 인증 정보를 넣어 줌으로써 현재 요청을 보낸 사용자가 인증된 사용자임을 Spring Security에게 알려준다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        log.info("jwt 필터 성공");
        //	필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }


    // http 요청에서 토큰만 추출 한다.
    private String resolveToken(HttpServletRequest request) {
        // 요청 헤더 중 Authorization 값을 가져온다
        // 요청 헤드의 생김새 Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0...

        log.info("resolveToken Service 도착 토큰 추출 ");
        log.info("request = {}", request);


        //엑세스 토큰이 있는지 확인
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                log.info("cookie = {}", cookie);
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue(); // 쿠키에서 accessToken 추출
                }
            }
        }
        return null;
    }

}
