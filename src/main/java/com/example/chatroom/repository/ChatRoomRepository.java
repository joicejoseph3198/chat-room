package com.example.chatroom.repository;

import com.example.chatroom.model.ChatRoom;

import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository {
    Optional<ChatRoom> findByName(final String chatRoomName);
    void save(final ChatRoom chatRoom);
    List<ChatRoom> getChatRoomListing();
    void deleteChatRoom(final String chatRoomName);
    void addParticipant(final String chatRoomName, final String participantName);
    void removeParticipant(final String chatRoomName, final String participantName);
}
