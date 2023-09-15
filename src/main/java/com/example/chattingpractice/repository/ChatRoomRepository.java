package com.example.chattingpractice.repository;

import com.example.chattingpractice.domain.ChatRoom;
import com.example.chattingpractice.service.RedisSubscriber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.*;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {

    private static final String CHAT_ROOMS = "CHAT_ROOM";

    // 채팅방 (topic) 에 발행되는 메시지를 처리할 Listener
    private final RedisMessageListenerContainer redisMessageListener;
    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보, 채팅방에 매치되는 topic 정보를 Map 에 넣어 roomId로 찾을 수 있도록 한다.
    private Map<String, ChannelTopic> topics;


    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    /**
     * 채팅방 생성: 서버간 채팅방 공유를 위해 redis hash 에 저장한다.
     */
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);

        // redis 에 topic 을 만들고 pub/sub 통신을 하기 위해 리스터를 설정
        ChannelTopic topic = new ChannelTopic(chatRoom.getRoomId());
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(chatRoom.getRoomId(), topic);

        return chatRoom;
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}
