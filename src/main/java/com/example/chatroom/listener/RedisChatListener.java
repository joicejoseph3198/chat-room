package com.example.chatroom.listener;

import com.example.chatroom.dto.MessageRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

// Configuring a listener for channels matching the pattern `CHATROOM:*`
@Component
@Slf4j
public class RedisChatListener implements MessageListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RedisChatListener(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    // Automatically called whenever a message is published to a redis channel that the listener is registered to
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Fetching channel and chatRoomName
        String channel = new String(message.getChannel());
        String chatRoomName = Optional.of(channel.split(":")[1]).orElse("");
        // Converting message to appropriate object
        String payload = new String(message.getBody());
        MessageRequestDTO messageRequestDTO = null;
        try {
            messageRequestDTO = objectMapper.readValue(payload, MessageRequestDTO.class);
            // Forwards to websocket topic
            messagingTemplate.convertAndSend("/topic/chatroom/"+ chatRoomName, messageRequestDTO);
            log.info("Message sent to web socket topic | Topic Name: `/topic/chatroom/{}`", chatRoomName);
        } catch (JsonProcessingException e) {
            log.error("Error converting message to appropriate object | Payload: {}", payload);
        }
    }
}
