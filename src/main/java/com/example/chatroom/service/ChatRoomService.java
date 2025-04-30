package com.example.chatroom.service;

import com.example.chatroom.dto.ChatRoomRequestDTO;
import com.example.chatroom.dto.ChatRoomResponseDTO;
import com.example.chatroom.model.ChatRoom;

import java.util.List;
import java.util.Set;


public interface ChatRoomService {
    ChatRoomResponseDTO<Object> createChatRoom(final ChatRoomRequestDTO requestDTO);
    ChatRoomResponseDTO<List<ChatRoom>> getChatRoomListing();
    ChatRoomResponseDTO<Object> deleteChatRoom(final String chatRoomId);
    ChatRoomResponseDTO<Object> joinChatRoom(final String chatRoomId, String participant);
    ChatRoomResponseDTO<Object> exitChatRoom(final String chatRoomId, String participant);
    ChatRoomResponseDTO<Set<String>> getActiveChatRoomListing(String participant);
}
