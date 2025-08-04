package com.est.mungpe.security.repository;


import com.est.mungpe.member.domain.Member;
import com.est.mungpe.security.domain.RefreshToken;

import java.util.Optional;

public interface TokenRepository {

    //토큰을 저장한다.
    RefreshToken save(Member member,String token);
    // token으로 유효한 토큰 찾기
    Optional<RefreshToken> findValidRefTokenByToken(String token);
    // MemberId로 유효한 토큰 찾기
    Optional<RefreshToken> findValidRefTokenByMemberId(Long memberId);
    // 블랙리스트 등록
    RefreshToken appendBlackList(RefreshToken refreshToken);
}
