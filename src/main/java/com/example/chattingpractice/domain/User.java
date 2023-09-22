package com.example.chattingpractice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String password;

    @OneToMany(mappedBy = "user")
    List<ChatRoomUser> chatRooms = new ArrayList<>();


    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
