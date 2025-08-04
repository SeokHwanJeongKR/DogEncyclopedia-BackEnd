package com.est.mungpe.security.repository.adapter;


import com.est.mungpe.member.domain.Member;
import com.est.mungpe.security.domain.RefreshToken;
import com.est.mungpe.security.domain.RefreshTokenBlackList;
import com.est.mungpe.security.repository.RefreshTokenBlackListRepository;
import com.est.mungpe.security.repository.RefreshTokenRepository;
import com.est.mungpe.security.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
// RefreshToken과 RefreshTokenBlackList를 중심으로, "토큰 저장", "조회", "블랙리스트 등록" 등의 로직을 캡슐화한 데이터 접근 계층
public class TokenRepositoryAdapter implements TokenRepository {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenBlackListRepository refreshTokenBlackListRepository;

    //새 RefreshToken을 생성하고 DB에 저장
    @Override
    public RefreshToken save(Member member, String token) {

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(token)
                .member(member)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    //문자열 토큰으로 DB 조회하고 블랙리스트인지 확인
    @Override
    public Optional<RefreshToken> findValidRefTokenByToken(String token) {
        // 토큰 조회
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByRefreshToken(token);

        // 토큰이 빈경우 refreshToken을 발급
        if (refreshTokenOptional.isEmpty()) {
            return refreshTokenOptional;
        }

        // 리프레쉬 토큰 정보 확인
        RefreshToken findToken = refreshTokenOptional.get();

        // 벤 여부 확인
        boolean isBanned = isBannedRefToken(findToken);

        // 밴 되었다면 재발급 거부.
        if (isBanned) {
            return Optional.empty();
        } else {
            //문제 없다면 발급
            return refreshTokenOptional;
        }
    }

    //회원 ID로 유효한 토큰 조회 (블랙리스트 제외 조건 내장)
    @Override
    public Optional<RefreshToken> findValidRefTokenByMemberId(Long memberId) {
        return refreshTokenRepository.findValidTokenByMemberId(memberId);
    }

    //	특정 RefreshToken을 블랙리스트에 등록
    @Override
    public RefreshToken appendBlackList(RefreshToken refreshToken) {

        RefreshTokenBlackList refreshTokenBlackList = RefreshTokenBlackList.builder()
                .refreshToken(refreshToken)
                .build();
        refreshTokenBlackListRepository.save(refreshTokenBlackList);

        return refreshToken;
    }

    //해당 토큰이 블랙리스트에 존재하는지 확인
    public boolean isBannedRefToken(RefreshToken refreshToken) {
        return refreshTokenBlackListRepository.existsByRefreshToken(refreshToken);
    }
}
