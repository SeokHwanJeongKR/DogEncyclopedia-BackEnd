package com.est.mungpe.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatBasicResponse {

    private Boolean result;
    private String message;

}
