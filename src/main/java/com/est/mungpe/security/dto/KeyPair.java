package com.est.mungpe.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeyPair {

    private String accessToken;
    private String refreshToken;
    // 쿠키에 값을 넣으려면 String 이여야 한다.
    private String memberId;
}
