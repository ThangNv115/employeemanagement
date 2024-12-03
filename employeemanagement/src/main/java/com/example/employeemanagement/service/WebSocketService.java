package com.example.employeemanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyStatusChange(Long userId, Integer status) {
        Map<String, Object> message = new HashMap<>();
        message.put("userId", userId);
        message.put("status", status);
        messagingTemplate.convertAndSend("/topic/status", message);
    }
}
