package com.est.mungpe.security.service.scheduler;

import com.est.mungpe.security.domain.RefreshToken;
import com.est.mungpe.security.repository.RefreshTokenBlackListRepository;
import com.est.mungpe.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class RefreshTokenScheduler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenBlackListRepository refreshTokenBlackListRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60) // 1분마다 실행
    public void deleteExpiredTokens() {

        log.info("스케쥴러 작동");

        //현재시간 설정
        LocalDateTime now = LocalDateTime.now();

        // 유효 시간이 now 보다 이전인 리프레시 토큰 리스트
        List<RefreshToken> expiredTokens = refreshTokenRepository.findByExpiredAtBefore(now);
        log.info("expiredTokens = {}", expiredTokens);
        if (!expiredTokens.isEmpty()) {

            // 유효 시간이 지난 리프레쉬 토큰 블랙 리스트 제거
            refreshTokenBlackListRepository.deleteByRefreshTokenIn(expiredTokens);
            // 유효 시간이 지난 리프레쉬 토큰 제거
            refreshTokenRepository.deleteByExpiredAtBefore(now);

            log.info("만료된 RefreshToken {}개 및 연관된 블랙리스트 삭제 완료", expiredTokens.size());
        } else {
            log.info("삭제할 만료된 RefreshToken 없음");
        }
    }
}
