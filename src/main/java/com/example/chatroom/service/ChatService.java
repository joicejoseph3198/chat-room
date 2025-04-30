package com.example.chatroom.service;

import com.example.chatroom.dto.ChatRoomResponseDTO;
import com.example.chatroom.dto.MessageAcknowledgementDTO;
import com.example.chatroom.dto.MessageDTO;
import com.example.chatroom.dto.MessageRequestDTO;

import java.util.List;

public interface ChatService {
    MessageAcknowledgementDTO sendMessageToRoom(final MessageRequestDTO messageRequest, String chatRoomName);
    ChatRoomResponseDTO<List<MessageDTO>> fetchMessageHistory(final int limit, final int offset, String chatRoomName);
}
