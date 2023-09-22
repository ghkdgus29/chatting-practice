package com.example.chattingpractice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String message;
    private LocalDateTime time;
    @OneToOne(fetch = FetchType.LAZY)
    private User sender;
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    public ChatMessage(String message, LocalDateTime time, User sender, ChatRoom chatRoom) {
        this.message = message;
        this.time = time;
        this.sender = sender;
        this.chatRoom = chatRoom;
    }
}
