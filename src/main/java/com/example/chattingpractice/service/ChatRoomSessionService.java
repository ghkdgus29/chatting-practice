package com.example.chattingpractice.service;

import com.example.chattingpractice.domain.ChatRoom;
import com.example.chattingpractice.domain.ChatRoomUser;
import com.example.chattingpractice.domain.User;
import com.example.chattingpractice.repository.ChatRoomRepository;
import com.example.chattingpractice.repository.ChatRoomUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatRoomSessionService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    private Map<String, ChatRoom> sessions = new HashMap<>();

    public ChatRoom findRoomById(String roomId) {
        if (sessions.containsKey(roomId)) {
            return sessions.get(roomId);
        }

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방 접근"));

        return sessions.put(chatRoom.getRoomId(), chatRoom);
    }

    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        sessions.put(chatRoom.getRoomId(), chatRoom);

        chatRoomRepository.save(chatRoom);

        return chatRoom;
    }

    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAll();
    }

    public List<ChatRoom> findMyRooms(User sessionUser) {
        return chatRoomUserRepository.findByUser(sessionUser).stream()
                .map(ChatRoomUser::getChatRoom)
                .collect(Collectors.toList());
    }
}
