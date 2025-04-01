package com.example.chatroom.controller;

import com.example.chatroom.dto.MessageAcknowledgementDTO;
import com.example.chatroom.dto.MessageRequestDTO;
import com.example.chatroom.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{roomId}")
    public MessageAcknowledgementDTO sendMessageToRoom(@Payload MessageRequestDTO chatMessage, @DestinationVariable String roomId) {
        return chatService.sendMessageToRoom(chatMessage, roomId);
    }
}
