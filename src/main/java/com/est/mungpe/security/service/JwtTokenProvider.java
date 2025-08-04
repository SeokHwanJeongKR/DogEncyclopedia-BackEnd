package com.est.mungpe.security.service;


import com.est.mungpe.member.domain.Member;
import com.est.mungpe.security.dto.KeyPair;
import com.est.mungpe.security.config.JwtConfiguration;
import com.est.mungpe.security.domain.RefreshToken;
import com.est.mungpe.security.dto.TokenBody;
import com.est.mungpe.security.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
//  jwt 토큰을 발급, 검증, 파싱 하는 클래스
public class JwtTokenProvider {
    // access Token과 Refresh 토큰의 재발급 정보를 담당
    private final JwtConfiguration configuration;

    // Refresh Token의 발급, 조회, 블랙리스트 등록을 담당
    private final TokenRepository refreshTokenRepositoryAdapter;

    // 시크릿 키 생성
    private SecretKey getSecretKey() {
        // JJWT 라이브러리의 유틸 클래스인 io.jsonwebtoken.security.Keys에서 제공하는 메서드로,HMAC 방식 서명을 위한 시크릿 키를 생성
        return Keys.hmacShaKeyFor(configuration.getSecret().getAppKey().getBytes());
    }

    //jwtToken 생성
    private String issue(Long memberId, String role, Long validTime) {

        // Payload = subject, claim("role"), issuedAt(iat), expiration(exp)
        // Signature  = signWith를 통해 생성
        // 아래의 코드에는 header에 해당되는 코드가 없는데 자동적으로 생성된다.  (alg: HS256, typ: JWT)
        String jwtToken = Jwts.builder()
                .setSubject(memberId.toString())                          // subject: 사용자 ID
                .claim("role", role)                                   // 사용자 역할(권한)을 추가
                .issuedAt(new Date())                                     // 발급 시간 (iat) 현재 시간
                .expiration(new Date(new Date().getTime() + validTime))   // 만료 시간 (ext) 현재 시간 + yml 설정 시간
                .signWith(getSecretKey(), Jwts.SIG.HS256)                 // 시크릿 키로 서명하여 Signature 생성
                .compact();                                               //  Header + Payload + Signature 결합 → 최종 JWT 문자열 반환

        return jwtToken;
    }

    // Accesss 토큰 생성
    public String issueAccessToken(Long memberId, String role) {
        return issue(memberId, role, configuration.getValidation().getAccess());
    }

    // Refresh 토큰 생성
    public String issueRefreshToken(Long memberId, String role) {
        return issue(memberId, role, configuration.getValidation().getRefresh());
    }

    // 토큰 두개를 묶는다.
    public KeyPair generateKeyPair(Member member) {

        String accessToken = issueAccessToken(member.getId(),member.getRole().name());

        String refreshToken = issueRefreshToken(member.getId(),member.getRole().name());

        refreshTokenRepositoryAdapter.save(member, refreshToken);

        KeyPair jwtTokens = KeyPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberId(member.getId().toString())
                .build();

        return jwtTokens;
    }

    //특정 사용자의 유효한 RefreshToken이 DB에 있는지 확인
    public RefreshToken validateRefreshToken(Long memberId) {

        Optional<RefreshToken> validRefTokenOptional = refreshTokenRepositoryAdapter.findValidRefTokenByMemberId(memberId);


        return validRefTokenOptional.orElse(null);
    }

    // 클라이언트가 보낸 JWT의 유효성을 서명(Signature) 기반으로 검증한다.
    // - parser(): JWT 문자열을 파싱할 준비를 한다.
    // - verifyWith(): 서명 검증에 사용할 SecretKey를 설정한다. (구버전은 setSigningKey() 사용 → 허용 타입 제한적)
    // - parseSignedClaims(): 토큰을 header.payload.signature로 분리하고, 서명이 유효한지 확인한다.
    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;

        // JWT 관련 최상위 예외	만료, 위조, 포맷 문제 등 대부분의 JWT 검증 실패 시 발생
        } catch (JwtException e) {
            log.info("JWT 토큰에 문제가 있습니다. = {}", e.getMessage());
            log.info("TOKEN : {}", token);

        // Java 기본 예외	null이거나 빈 토큰이 들어온 경우
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 Null입니다. = {}", e.getMessage());

        // 모든 나머지 예외 예상 못 한 오류 (시스템 오류 등)
        } catch (Exception e) {
            log.info("JWT 토큰 검증 중 예상치 못한 예외가 발생 했습니다. = {}", e.getMessage());
        }

        return false;
    }

    //토큰 내부 정보를 파싱해서 사용자 ID (sub)와 역할 (role)을 꺼냄
    public TokenBody parseJwt(String token) {
        Jws<Claims> parsed = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);

        return new TokenBody(
                // 토큰을 만들때 payload의 Subject 에 setter 로 memeberId를 넣었음, memberId 반환 받기
                Long.parseLong(parsed.getPayload().getSubject()),
                // role이라는 커스텀 Claim을 가져온다.
                parsed.getPayload().get("role").toString()
        );
    }

}
