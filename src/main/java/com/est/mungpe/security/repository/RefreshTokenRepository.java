package com.est.mungpe.security.repository;

import com.est.mungpe.security.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    //리프레쉬 토큰을 가져온다. 토큰의 별칭은 rt이다
    //RefreshTokenBlackList를 rt에 LEFT JOIN 한다. rt가 블랙리스트에 등록되어 있다면 rtb에 매칭되고, 아니면 rtb는 null이다.
    //지정한 사용자만 조회한다 (member.id = : memberId)
    //블랙리스트에 id가 없는 것만 조회 한다.
    @Query("""
    SELECT rt FROM RefreshToken rt
    LEFT JOIN RefreshTokenBlackList  rtb ON rtb.refreshToken = rt
    WHERE rt.member.id = :memberId
    AND rtb.id IS NULL
    """)
    Optional<RefreshToken> findValidTokenByMemberId(Long memberId);

    List<RefreshToken> findByExpiredAtBefore(LocalDateTime now);

    void deleteByExpiredAtBefore(LocalDateTime now);
}
