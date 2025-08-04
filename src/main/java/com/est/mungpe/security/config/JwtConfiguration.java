package com.est.mungpe.security.config;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtConfiguration {

    // Access Token과 Refresh Token의 유효 시간 설정을 담고 있다.
    // 값은 application.yml에서 설정된다.
    private final Validation validation;

    // 시크릿 키를 두 개로 분리해서 관리한다.
    // - appKey: 현재 사용 중인 시크릿 키
    // - originKey: 이전에 사용하던 시크릿 키 (기존 토큰 검증용)
    private final Secret secret;

    // 왜 굳이 내부 클래스로 정의했는가?
    // Validation은 JwtConfiguration 내부에서만 사용되며,
    // 다른 클래스에서 독립적으로 사용될 일이 없기 때문이다.
    // 관련 설정을 그룹화하여 구성의 명확성과 가독성을 높이기 위함이다.
    @Data
    public static class Validation {
        private Long access;
        private Long refresh;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Secret {
        private final String appKey;
        private final String originKey;
    }
}
