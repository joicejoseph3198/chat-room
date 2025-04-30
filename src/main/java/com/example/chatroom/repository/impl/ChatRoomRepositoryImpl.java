package com.example.chatroom.repository.impl;

import com.example.chatroom.model.ChatRoom;
import com.example.chatroom.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChatRoomRepositoryImpl implements ChatRoomRepository {
    private final RedisTemplate<String,Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ChatRoomRepositoryImpl(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<ChatRoom> findByName(final String chatRoomName) {
        Object redisValue = redisTemplate.opsForHash().get("CHATROOM",chatRoomName);
        if (redisValue == null) {
            return Optional.empty();
        }
        return Optional.of(objectMapper.convertValue(redisValue, ChatRoom.class));

    }

    @Override
    public void save(final ChatRoom chatRoom) {
        redisTemplate.opsForHash().put("CHATROOM",chatRoom.getName(), chatRoom);
    }

    @Override
    public List<ChatRoom> getChatRoomListing() {
        Map<Object,Object> hashEntries = redisTemplate.opsForHash().entries("CHATROOM");
        if(!CollectionUtils.isEmpty(hashEntries)){
            return hashEntries.values().stream()
                    .map(obj -> objectMapper.convertValue(obj, ChatRoom.class))
                    .map(chatRoom -> {
                        chatRoom.setName(chatRoom.getName().toUpperCase());
                        return chatRoom;
                    }).toList();
        }
        return List.of();
    }

    @Override
    public void deleteChatRoom(String chatRoomName) {
        redisTemplate.opsForHash().delete("CHATROOM", chatRoomName);
        redisTemplate.delete("PARTICIPANTS:" + chatRoomName);
    }

    @Override
    public boolean addParticipant(String chatRoomName, String participantName) {
        if(Boolean.FALSE.equals(redisTemplate.opsForSet().isMember("PARTICIPANTS:" + chatRoomName, participantName))){
            redisTemplate.opsForSet().add("PARTICIPANTS:" + chatRoomName, participantName);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public boolean removeParticipant(String chatRoomName, String participantName) {
        if(Boolean.TRUE.equals(redisTemplate.opsForSet().isMember("PARTICIPANTS:" + chatRoomName, participantName))){
            redisTemplate.opsForSet().remove("PARTICIPANTS:" + chatRoomName, participantName);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Set<String> getAllParticipants(String chatRoomName){
        Set<Object> entries = redisTemplate.opsForSet().members("PARTICIPANTS:" + chatRoomName);
        if(!CollectionUtils.isEmpty(entries)){
            return entries.stream()
                    .map(obj -> objectMapper.convertValue(obj, String.class))
                    .map(String::toUpperCase)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    public void addActiveChatRoom(String chatRoomName, String participantName) {
        if(Boolean.FALSE.equals(redisTemplate.opsForSet().isMember("ACTIVE_CHAT_ROOM:" + participantName, chatRoomName))){
            redisTemplate.opsForSet().add("ACTIVE_CHAT_ROOM:" + participantName, chatRoomName);
        }
    }

    public void removeActiveChatRoom(String chatRoomName, String participantName) {
        if(Boolean.TRUE.equals(redisTemplate.opsForSet().isMember("ACTIVE_CHAT_ROOM:" + participantName, chatRoomName))){
            redisTemplate.opsForSet().remove("ACTIVE_CHAT_ROOM:" + participantName, chatRoomName);
        }
    }

    public Set<String> getAllActiveChatRooms(String participantName){
        Set<Object> entries = redisTemplate.opsForSet().members("ACTIVE_CHAT_ROOM:" + participantName);
        if(!CollectionUtils.isEmpty(entries)){
            return entries.stream()
                    .map(obj -> objectMapper.convertValue(obj, String.class))
                    .map(String::toUpperCase)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }
}
