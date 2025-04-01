package com.example.chatroom.repository;

import com.example.chatroom.dto.MessageDTO;
import com.example.chatroom.dto.MessageRequestDTO;

import java.util.List;

public interface ChatRepository {
    void updateChatHistory(final MessageRequestDTO messageRequestDTO, final String chatRoomName);
    List<MessageDTO> fetchChatHistory(final int count, final String chatRoomName);
}
