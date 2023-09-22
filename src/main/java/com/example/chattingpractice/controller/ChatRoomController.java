package com.example.chattingpractice.controller;

import com.example.chattingpractice.domain.ChatRoom;
import com.example.chattingpractice.domain.User;
import com.example.chattingpractice.service.ChatRoomSessionService;
import com.example.chattingpractice.util.SessionConstant;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatRoomSessionService chatRoomSessionService;

    @GetMapping("/chat/room")
    public String rooms(Model model, HttpSession session) {
        if (session.getAttribute(SessionConstant.SESSION_USER) == null) {
            return "redirect:/login";
        }

        return "chat/room";
    }

    @GetMapping("/chat/myRoom")
    public String myRooms(Model model, HttpSession session) {
        if (session.getAttribute(SessionConstant.SESSION_USER) == null) {
            return "redirect:/login";
        }

        return "chat/myRoom";
    }

    @GetMapping("/chat/rooms")
    @ResponseBody
    public List<ChatRoom> rooms() {
        return chatRoomSessionService.findAllRoom();
    }

    @GetMapping("/chat/myRooms")
    @ResponseBody
    public List<ChatRoom> myRooms(HttpSession session) {
        User sessionUser = (User) session.getAttribute(SessionConstant.SESSION_USER);
        return chatRoomSessionService.findMyRooms(sessionUser);
    }

    @PostMapping("/chat/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomSessionService.createChatRoom(name);
    }

    @GetMapping("/chat/room/enter/{roomId}")
    public String enterRoom(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "chat/roomdetail";
    }

    @GetMapping("/chat/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomSessionService.findRoomById(roomId);
    }
}
