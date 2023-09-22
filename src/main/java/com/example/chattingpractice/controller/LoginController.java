package com.example.chattingpractice.controller;

import com.example.chattingpractice.domain.User;
import com.example.chattingpractice.domain.dto.LoginForm;
import com.example.chattingpractice.repository.UserRepository;
import com.example.chattingpractice.util.SessionConstant;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "/chat/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm loginForm, HttpSession session) {

        User savedUser = userRepository.findByName(loginForm.getName())
                .orElseGet(() -> userRepository.save(new User(loginForm.getName(), loginForm.getPassword())));

        session.setAttribute(SessionConstant.SESSION_USER, savedUser);

        return "redirect:/chat/room";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}
