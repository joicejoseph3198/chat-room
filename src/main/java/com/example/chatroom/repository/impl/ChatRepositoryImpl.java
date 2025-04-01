package com.example.chatroom.repository.impl;
import com.example.chatroom.dto.MessageDTO;
import com.example.chatroom.dto.MessageRequestDTO;
import com.example.chatroom.enums.MessageType;
import com.example.chatroom.repository.ChatRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.List;

@Component
public class ChatRepositoryImpl implements ChatRepository {

    private final RedisTemplate<String,Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ChatRepositoryImpl(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void updateChatHistory(final MessageRequestDTO messageRequestDTO, String chatRoomName) {
        if(messageRequestDTO.type().equals(MessageType.CHAT)){
            MessageDTO message = new MessageDTO(messageRequestDTO.participant(), messageRequestDTO.message(), messageRequestDTO.timestamp());
            redisTemplate.opsForList().rightPush("CHAT_HISTORY:"+ chatRoomName.toLowerCase(), message);
        }
    }

    @Override
    public List<MessageDTO> fetchChatHistory(final int count, String chatRoomName) {
        List<Object> chatHistory = redisTemplate.opsForList().range("CHAT_HISTORY:"+chatRoomName.toLowerCase(), -count,-1);
        if(CollectionUtils.isEmpty(chatHistory)){
            return List.of();
        }
        return chatHistory.stream().map(message -> objectMapper.convertValue(message, MessageDTO.class)).toList();
    }
}
