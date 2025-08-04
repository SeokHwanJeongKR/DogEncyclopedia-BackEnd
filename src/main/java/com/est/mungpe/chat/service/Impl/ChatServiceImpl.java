package com.est.mungpe.chat.service.Impl;

import com.est.mungpe.chat.domain.Chat;
import com.est.mungpe.chat.dto.ChatBasicResponse;
import com.est.mungpe.chat.dto.MessageDto;
import com.est.mungpe.chat.dto.SendMessageRequest;
import com.est.mungpe.chat.dto.getAllRoomResponse;
import com.est.mungpe.chat.repository.ChatRepository;
import com.est.mungpe.chat.service.ChatService;
import com.est.mungpe.exception.ExceptionMessage;
import com.est.mungpe.exception.NoSameSenderAndReciverException;
import com.est.mungpe.exception.NotAdminException;
import com.est.mungpe.member.domain.Member;
import com.est.mungpe.member.domain.Role;
import com.est.mungpe.member.service.MemberService;
import com.est.mungpe.system.utill.SecurityUtill;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final MemberService memberService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<MessageDto> openChat() {
        Member admin = memberService.getMemberByEmail("jsver12@gmail.com");
        Long adminId = admin.getId();

        Long loginUserId = SecurityUtill.getCurrentMemberId();
        String roomId = "room" + loginUserId;

        boolean talkedBefore = chatRepository.existsChatBetweenUsers(loginUserId, adminId);

        if (!talkedBefore) {
            Chat chat = Chat.builder()
                    .senderNum(adminId)
                    .receiverNum(loginUserId)
                    .message("무엇을 도와 드릴까요?")
                    .roomId(roomId)
                    .build();
            chatRepository.save(chat);
        }

        List<Chat> chatList = chatRepository.findAllChatByRoomId(roomId);

        return chatList.stream().map(chat -> MessageDto.builder()
                .senderNum(chat.getSenderNum())
                .receiverNum(chat.getReceiverNum())
                .message(chat.getMessage())
                .roomId(chat.getRoomId())
                .sendTime(chat.getSendTime())
                .build()
        ).toList();
    }

    @Override
    public List<MessageDto> getChatWithUser(Long userId) {

        Long adminId = SecurityUtill.getCurrentMemberId();

        String roomId = "room" + userId;

        Member member = memberService.getMemberById(adminId);
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new NotAdminException(ExceptionMessage.NOT_ADMIN);
        }

        List<Chat> chatList = chatRepository.findAllChatByRoomId(roomId);

        return chatList.stream().map(chat -> MessageDto.builder()
                .senderNum(chat.getSenderNum())
                .receiverNum(chat.getReceiverNum())
                .message(chat.getMessage())
                .roomId(chat.getRoomId())
                .sendTime(chat.getSendTime())
                .build()
        ).toList();
    }

    @Override
    public ChatBasicResponse sendChat(SendMessageRequest request) {
        Long loginId = SecurityUtill.getCurrentMemberId();
        Member admin = memberService.getMemberByEmail("jsver12@gmail.com");
        Long adminId = admin.getId();

        Long receiverId;
        Long senderId;
        String roomId;

        if (!loginId.equals(adminId)) {
            // 고객이 보냄
            receiverId = adminId;
            senderId = loginId;
            roomId = "room" + loginId;
        } else {
            // 관리자가 보냄
            senderId = adminId;
            receiverId = request.getReceiverNum();
            roomId = "room" + receiverId;
        }

        if (receiverId.equals(senderId)) {
            throw new NoSameSenderAndReciverException(ExceptionMessage.NO_SAME_SANDER_RECEIVER);
        }

        Chat chat = Chat.builder()
                .senderNum(senderId)
                .receiverNum(receiverId)
                .message(request.getMessage())
                .roomId(roomId)
                .build();
        chatRepository.save(chat);

        // WebSocket
        MessageDto message = MessageDto.builder()
                .senderNum(senderId)
                .receiverNum(receiverId)
                .message(request.getMessage())
                .roomId(roomId)
                .sendTime(chat.getSendTime())
                .build();
        messagingTemplate.convertAndSend("/topic/" + roomId, message);

        log.info("웹소켓 발송: roomId={}, message={}", roomId, message);

        return ChatBasicResponse.builder()
                .result(true)
                .message("Success Send Message")
                .build();
    }

    @Override
    public getAllRoomResponse getChatList() {

        List<String> allRoomIds = chatRepository.findAllRoomIds();

        return getAllRoomResponse.builder()
                .roomIds(allRoomIds)
                .result(true)
                .message("Success Get All Room List")
                .build();

    }

}
