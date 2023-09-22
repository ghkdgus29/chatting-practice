package com.example.chattingpractice.interceptor;

import com.example.chattingpractice.domain.User;
import com.example.chattingpractice.util.SessionConstant;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Slf4j
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
        HttpSession session = req.getServletRequest().getSession();
        User sessionUser = (User) session.getAttribute(SessionConstant.SESSION_USER);
        attributes.put(SessionConstant.SESSION_USER, sessionUser);
        return true;
    }
}
