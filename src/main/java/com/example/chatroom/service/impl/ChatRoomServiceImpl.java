package com.example.chatroom.service.impl;

import com.example.chatroom.dto.ChatRoomRequestDTO;
import com.example.chatroom.dto.ChatRoomResponseDTO;
import com.example.chatroom.dto.MessageRequestDTO;
import com.example.chatroom.enums.MessageType;
import com.example.chatroom.model.ChatRoom;
import com.example.chatroom.repository.ChatRoomRepository;
import com.example.chatroom.service.ChatRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate<String,Object> redisTemplate;

    @Autowired
    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, RedisTemplate<String, Object> redisTemplate) {
        this.chatRoomRepository = chatRoomRepository;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public ChatRoomResponseDTO<Object> createChatRoom(final ChatRoomRequestDTO requestDTO) {
        log.info("Attempting to create chat room `{}` ", requestDTO.name());
        Optional<ChatRoom> conflictingRoom = chatRoomRepository.findByName(requestDTO.name().toLowerCase());
        if(conflictingRoom.isPresent()){
            log.warn("Conflicting name found | Operation unsuccessful");
            return new ChatRoomResponseDTO<>(null, "Chat room with the given name already exists.","unsuccessful",null);
        }else{
            ChatRoom newEntry =
                    ChatRoom.builder()
                            .name(requestDTO.name().toLowerCase())
                            .owner(requestDTO.owner())
                            .timestamp(System.currentTimeMillis())
                    .build();
            chatRoomRepository.save(newEntry);
            log.info("Chat room created | Timestamp: {}", newEntry.getTimestamp());
            return new ChatRoomResponseDTO<>(newEntry.getName(),"Chat room '" + newEntry.getName() + "' created successfully.", "success",null);
        }
    }

    @Override
    public ChatRoomResponseDTO<List<ChatRoom>> getChatRoomListing() {
        return new ChatRoomResponseDTO<>(null,"Available chat rooms","success", chatRoomRepository.getChatRoomListing());
    }

    @Override
    public ChatRoomResponseDTO<Object> deleteChatRoom(final String chatRoomName) {
        log.info("Terminating chatroom | {}", chatRoomName);
        MessageRequestDTO generatedMessage = new MessageRequestDTO("ALL","Chat room has been terminated by the owner", System.currentTimeMillis(), MessageType.TERMINATED);
        redisTemplate.convertAndSend("CHATROOM:"+chatRoomName, generatedMessage);
        chatRoomRepository.deleteChatRoom(chatRoomName.toLowerCase());
        return new ChatRoomResponseDTO<>(chatRoomName,"Chatroom terminated","success", null);
    }

    @Override
    public ChatRoomResponseDTO<Object> joinChatRoom(final String chatRoomName, final String participant) {
        log.info("Attempting to connect participant to chat room | Participant: `{}` | Chat Room : `{}`  ", participant, chatRoomName.toLowerCase());
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByName(chatRoomName.toLowerCase());
        if(chatRoom.isEmpty()){
            log.info("Failed to connect participant to chat room | Participant: {} | Chat Room : {}  ", participant, chatRoomName.toLowerCase());
            return new ChatRoomResponseDTO<>(null, "Given chat room could not be found", "unsuccessful", null);
        }
        chatRoomRepository.addParticipant(chatRoomName.toLowerCase(),participant);
        MessageRequestDTO generatedMessage = new MessageRequestDTO(participant,String.format("`%s` has joined the chat room", participant),System.currentTimeMillis(), MessageType.JOINED);
        redisTemplate.convertAndSend("CHATROOM:"+chatRoomName, generatedMessage);
        log.info("Successfully connected participant to chat room | Participant: `{}` | Chat Room : `{}`  ", participant, chatRoomName.toLowerCase());
        return new ChatRoomResponseDTO<>(null, "Participant '" + participant +"' joined chat room '" + chatRoomName +"'.", "success", null);
    }

    @Override
    public ChatRoomResponseDTO<Object> exitChatRoom(final String chatRoomName, final String participant) {
        log.info("Attempting to exit chat room | Participant: `{}` | Chat Room : `{}`  ", participant, chatRoomName.toLowerCase());
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByName(chatRoomName.toLowerCase());
        if(chatRoom.isEmpty()){
            log.info("Non existent chat room | Participant: {} | Chat Room : {}  ", participant, chatRoomName.toLowerCase());
            return new ChatRoomResponseDTO<>(null, "Given chat room could not be found", "unsuccessful", null);
        }
        chatRoomRepository.removeParticipant(chatRoomName.toLowerCase(),participant);
        MessageRequestDTO generatedMessage = new MessageRequestDTO(participant,String.format("`%s` has left the chat room", participant),System.currentTimeMillis(), MessageType.LEFT);
        redisTemplate.convertAndSend("CHATROOM:"+chatRoomName, generatedMessage);
        log.info("Successfully exited chat room | Participant: `{}` | Chat Room : `{}`  ", participant, chatRoomName.toLowerCase());
        return new ChatRoomResponseDTO<>(null, "Participant '" + participant +"' joined chat room '" + chatRoomName +"'.", "success", null);
    }
}
