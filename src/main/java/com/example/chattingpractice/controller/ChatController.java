package com.example.chattingpractice.controller;

import com.example.chattingpractice.domain.ChatRoom;
import com.example.chattingpractice.domain.ChatRoomUser;
import com.example.chattingpractice.domain.User;
import com.example.chattingpractice.domain.dto.ChatMessageDTO;
import com.example.chattingpractice.repository.ChatRoomRepository;
import com.example.chattingpractice.repository.ChatRoomUserRepository;
import com.example.chattingpractice.service.ChatRoomSessionService;
import com.example.chattingpractice.util.SessionConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatRoomSessionService chatRoomSessionService;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat/enter")
    public void enterRoom(ChatMessageDTO message, SimpMessageHeaderAccessor messageHeaderAccessor) {
        User sessionUser = (User) messageHeaderAccessor.getSessionAttributes().get(SessionConstant.SESSION_USER);

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(message.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방에 접근하여 메시지 전송"));

        if (chatRoomUserRepository.existsByUserAndChatRoom(sessionUser, chatRoom)) {
            return;
        }

        chatRoomUserRepository.save(new ChatRoomUser(chatRoom, sessionUser));

        message.setMessage("나님 입장!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        message.setSender(sessionUser.getName());
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }


    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message, SimpMessageHeaderAccessor messageHeaderAccessor) {
        User sessionUser = (User) messageHeaderAccessor.getSessionAttributes().get(SessionConstant.SESSION_USER);

        message.setSender(sessionUser.getName());
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
