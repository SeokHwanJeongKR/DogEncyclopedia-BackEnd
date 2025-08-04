package com.est.mungpe.chat.dto;

import lombok.Data;

@Data
public class SendMessageRequest {

    private Long receiverNum;
    private String message;
}
