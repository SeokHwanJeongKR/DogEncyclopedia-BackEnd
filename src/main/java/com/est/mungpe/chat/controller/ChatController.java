package com.est.mungpe.chat.controller;


import com.est.mungpe.chat.dto.ChatBasicResponse;
import com.est.mungpe.chat.dto.MessageDto;
import com.est.mungpe.chat.dto.SendMessageRequest;
import com.est.mungpe.chat.dto.getAllRoomResponse;
import com.est.mungpe.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<MessageDto>> openChat() {
        log.info("Open Chat Controller 도착");

        List<MessageDto> result = chatService.openChat();

        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/with/{userId}")
    public ResponseEntity<List<MessageDto>> getAdminChat(@PathVariable Long userId) {

        log.info("Admin Chat Controller 도착");

        List<MessageDto> result = chatService.getChatWithUser(userId);

        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "send")
    public ResponseEntity<ChatBasicResponse> sendMessage(@RequestBody SendMessageRequest request) {

        log.info("Send Controller 도착 {}", request);

        ChatBasicResponse result = chatService.sendChat(request);

        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "list")
    public ResponseEntity<getAllRoomResponse> getChatList() {

        log.info("getAllRoomList Controller 도착 {}");

        getAllRoomResponse result = chatService.getChatList();

        log.info("result = {}", result);

        return ResponseEntity.ok(result);


    }
}
