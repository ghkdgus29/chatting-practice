package com.example.chattingpractice.controller;

import com.example.chattingpractice.domain.ChatMessage;
import com.example.chattingpractice.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            headerAccessor.getSessionAttributes().put("username", message.getSender());
            headerAccessor.getSessionAttributes().put("roomId", message.getRoomId());

            chatRoomRepository.findRoomById(message.getRoomId()).increaseUserCount();
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("DisConn {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        chatRoomRepository.findRoomById(roomId).minusUserCount();

        ChatMessage chat = ChatMessage.create(username, username + "님이 퇴장하였습니다.");

        messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, chat);
    }
}
