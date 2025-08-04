package com.est.mungpe.pedia.domain;

import com.est.mungpe.pedia.dto.PediaBasicRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PediaEditRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pediaId;
    private String target;
    private String originalInfo;
    private String newInfo;
    private LocalDateTime createdAt = LocalDateTime.now();

    @Setter
    private boolean completed = false;

    @Builder
    public PediaEditRequest(Long pediaId, String target, String originalInfo, String newInfo) {
        this.pediaId = pediaId;
        this.target = target;
        this.originalInfo = originalInfo;
        this.newInfo = newInfo;
    }

}
