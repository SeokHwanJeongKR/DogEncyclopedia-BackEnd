package com.est.mungpe.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class getAllRoomResponse {

    private List<String> roomIds;
    private boolean result;
    private String message;
}
