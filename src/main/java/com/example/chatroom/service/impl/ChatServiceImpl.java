package com.example.chatroom.service.impl;

import com.example.chatroom.dto.ChatRoomResponseDTO;
import com.example.chatroom.dto.MessageAcknowledgementDTO;
import com.example.chatroom.dto.MessageDTO;
import com.example.chatroom.dto.MessageRequestDTO;
import com.example.chatroom.repository.ChatRepository;
import com.example.chatroom.repository.ChatRoomRepository;
import com.example.chatroom.service.ChatService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository messageRepository;
    private final RedisTemplate<String,Object> redisTemplate;

    @Autowired
    public ChatServiceImpl(ChatRoomRepository chatRoomRepository, ChatRepository messageRepository, RedisTemplate<String, Object> redisTemplate) {
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
        this.redisTemplate = redisTemplate;
    }

    private boolean validateMessageRequest(final MessageRequestDTO messageRequestDTO){
        return !StringUtils.isEmpty(messageRequestDTO.message()) && !messageRequestDTO.participant().isBlank() &&
                            !Objects.isNull(messageRequestDTO.timestamp()) && !messageRequestDTO.type().name().isBlank();
    }

    private boolean isMember(final String participantName, final String chatRoomName){
        return chatRoomRepository.getAllParticipants(chatRoomName).contains(participantName.toUpperCase());
    }

    @Override
    public MessageAcknowledgementDTO sendMessageToRoom(final MessageRequestDTO messageRequest, String chatRoomName) {
        boolean isValid = validateMessageRequest(messageRequest);
        if(!isValid){
            log.error("Invalid message format. Delivery unsuccessful");
            return new MessageAcknowledgementDTO(messageRequest.participant(),"Improper message format. Message couldn't be delivered", "unsuccessful", System.currentTimeMillis());
        }
        if(!isMember(messageRequest.participant(), chatRoomName)){
            log.error("Not a member of the chatroom. Cannot send message to the chatroom");
            return new MessageAcknowledgementDTO(messageRequest.participant(), "Cannot send message to the chatroom. Please join the chatroom before sending a message.", "unsuccessful", System.currentTimeMillis());
        }
        if(chatRoomRepository.findByName(chatRoomName).isEmpty()){
            log.error("Chat room not found. Delivery unsuccessful. | Chat Room : `{}`", chatRoomName);
            return new MessageAcknowledgementDTO(messageRequest.participant(),String.format("Chat room `%s` not found. Message couldn't be delivered.", chatRoomName), "unsuccessful", System.currentTimeMillis());
        }
        redisTemplate.convertAndSend("CHATROOM:"+chatRoomName, messageRequest);
        messageRepository.updateChatHistory(messageRequest,chatRoomName);
        log.info("Message delivery successful. Message propagated to redis channel | Channel: `CHATROOM:{}`", chatRoomName);
        return new MessageAcknowledgementDTO(messageRequest.participant(),"Message delivered successfully", "success", System.currentTimeMillis());
    }

    public ChatRoomResponseDTO<List<MessageDTO>> fetchMessageHistory(final int limit, final int offset, String chatRoomName){
       List<MessageDTO> chatHistory = messageRepository.fetchChatHistory(limit,offset,chatRoomName);
       return new ChatRoomResponseDTO<>(chatRoomName,"Message history fetched successfully", "success", chatHistory);
    }
}
