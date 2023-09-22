package com.example.chattingpractice.domain;

import jakarta.persistence.*;

@Entity
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatMessage chatMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
