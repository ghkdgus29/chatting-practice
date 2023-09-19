package com.example.chattingpractice.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatRoom {

    private String roomId;
    private String name;
    private int userCount;

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        chatRoom.userCount = 0;
        return chatRoom;
    }

    public void increaseUserCount() {
        userCount += 1;
    }

    public void minusUserCount() {
        userCount -= 1;
    }
}
