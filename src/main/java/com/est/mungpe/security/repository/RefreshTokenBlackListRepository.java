package com.est.mungpe.security.repository;

import com.est.mungpe.security.domain.RefreshToken;
import com.est.mungpe.security.domain.RefreshTokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenBlackListRepository extends JpaRepository<RefreshTokenBlackList, Long> {

    // 해당 refreshToken이 DB에 있는지 확인
    boolean existsByRefreshToken(RefreshToken refreshToken);

    void deleteByRefreshTokenIn(java.util.List<RefreshToken> refreshTokens);
}
