package com.example.chatroom.service;

import com.example.chatroom.dto.MessageAcknowledgementDTO;
import com.example.chatroom.dto.MessageRequestDTO;

public interface ChatService {
    MessageAcknowledgementDTO sendMessageToRoom(final MessageRequestDTO messageRequest, String chatRoomName);
}
