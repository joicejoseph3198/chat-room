package com.example.chatroom.repository;

import com.example.chatroom.model.ChatRoom;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface ChatRoomRepository {
    Optional<ChatRoom> findByName(final String chatRoomName);
    void save(final ChatRoom chatRoom);
    List<ChatRoom> getChatRoomListing();
    void deleteChatRoom(final String chatRoomName);
    boolean addParticipant(final String chatRoomName, final String participantName);
    boolean removeParticipant(final String chatRoomName, final String participantName);
    Set<String> getAllParticipants(final String chatRoomName);
    public void addActiveChatRoom(String chatRoomName, String participantName);
    public void removeActiveChatRoom(String chatRoomName, String participantName);
    public Set<String> getAllActiveChatRooms(String participantName);
}
