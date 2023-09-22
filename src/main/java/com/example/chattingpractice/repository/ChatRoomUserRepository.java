package com.example.chattingpractice.repository;

import com.example.chattingpractice.domain.ChatRoom;
import com.example.chattingpractice.domain.ChatRoomUser;
import com.example.chattingpractice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Integer> {

    List<ChatRoomUser> findByUser(User user);

    boolean existsByUserAndChatRoom(User user, ChatRoom chatRoom);

}
