package com.est.mungpe.chat.service;

import com.est.mungpe.chat.dto.ChatBasicResponse;
import com.est.mungpe.chat.dto.MessageDto;
import com.est.mungpe.chat.dto.SendMessageRequest;
import com.est.mungpe.chat.dto.getAllRoomResponse;

import java.util.List;

public interface ChatService {
    List<MessageDto> openChat();

    List<MessageDto> getChatWithUser(Long userId);

    ChatBasicResponse sendChat(SendMessageRequest request);

    getAllRoomResponse getChatList();
}
