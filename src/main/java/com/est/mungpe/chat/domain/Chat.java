package com.est.mungpe.chat.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long receiverNum;

    private Long senderNum;

    private String message;

    private String roomId;

    private LocalDateTime sendTime = LocalDateTime.now();

    @Builder
    public Chat (Long receiverNum, Long senderNum, String message, String roomId) {
        this.receiverNum = receiverNum;
        this.senderNum = senderNum;
        this.message = message;
        this.roomId = roomId;

    }

}
