package com.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * WebSocket controller for real-time communication
 * Handles WebSocket messages and broadcasts updates
 */
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Handle subscription to dashboard updates
     */
    @MessageMapping("/subscribe")
    @SendTo("/topic/dashboard")
    public String subscribe(String message) {
        return "Subscribed to dashboard updates: " + message;
    }

    /**
     * Handle client ping messages
     */
    @MessageMapping("/ping")
    @SendTo("/topic/pong")
    public String ping(String message) {
        return "pong: " + System.currentTimeMillis();
    }

    /**
     * Send system status update
     */
    public void sendSystemStatus(String status) {
        messagingTemplate.convertAndSend("/topic/system/status", status);
    }

    /**
     * Send error notification
     */
    public void sendErrorNotification(String error) {
        messagingTemplate.convertAndSend("/topic/system/errors", error);
    }

    /**
     * Send custom notification to all clients
     */
    public void sendNotification(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}