package com.example.chattingpractice.repository;

import com.example.chattingpractice.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

    Optional<ChatRoom> findByRoomId(String roomId);

}
