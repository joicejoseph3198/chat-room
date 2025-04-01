package com.example.chatroom.repository.impl;

import com.example.chatroom.model.ChatRoom;
import com.example.chatroom.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                    .toList();
        }
        return List.of();
    }

    @Override
    public void deleteChatRoom(String chatRoomName) {
        redisTemplate.opsForHash().delete("CHATROOM", chatRoomName);
        redisTemplate.delete("PARTICIPANTS:" + chatRoomName);
    }

    @Override
    public void addParticipant(String chatRoomName, String participantName) {
        if(Boolean.FALSE.equals(redisTemplate.opsForSet().isMember("PARTICIPANTS:" + chatRoomName, participantName))){
            redisTemplate.opsForSet().add("PARTICIPANTS:" + chatRoomName, participantName);
        }
    }

    public void removeParticipant(String chatRoomName, String participantName) {
        redisTemplate.opsForSet().remove("PARTICIPANTS:" + chatRoomName, participantName);
    }
}
