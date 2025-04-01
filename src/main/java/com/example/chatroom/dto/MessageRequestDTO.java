package com.example.chatroom.dto;

import com.example.chatroom.enums.MessageType;

public record MessageRequestDTO(String participant, String message, Long timestamp, MessageType type) {
}
