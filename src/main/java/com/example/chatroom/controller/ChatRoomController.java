package com.example.chatroom.controller;

import com.example.chatroom.dto.ChatRoomRequestDTO;
import com.example.chatroom.dto.ChatRoomResponseDTO;
import com.example.chatroom.dto.MessageDTO;
import com.example.chatroom.model.ChatRoom;
import com.example.chatroom.service.ChatRoomService;
import com.example.chatroom.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    public ChatRoomController(ChatRoomService chatRoomService, ChatService chatService) {
        this.chatRoomService = chatRoomService;
        this.chatService = chatService;
    }

    @PostMapping("/")
    public ChatRoomResponseDTO<Object> createChatRoom(@Valid @RequestBody ChatRoomRequestDTO chatRoomRequestDTO){
        return chatRoomService.createChatRoom(chatRoomRequestDTO);
    }

    @GetMapping("/all")
    public ChatRoomResponseDTO<List<ChatRoom>> getChatRoomListing(){
        return chatRoomService.getChatRoomListing();
    }

    @DeleteMapping("/{roomId}")
    public ChatRoomResponseDTO<Object> terminateChatRoom(@PathVariable(name = "roomId") String roomId){
        return chatRoomService.deleteChatRoom(roomId);
    }

    @PostMapping("/{roomId}/join")
    public ChatRoomResponseDTO<Object> joinChatRoom(@PathVariable(name = "roomId") String roomId,
                                                    @RequestParam(value = "participant", required = true) String participant){
        return chatRoomService.joinChatRoom(roomId, participant);
    }

    @PostMapping("/{roomId}/exit")
    public ChatRoomResponseDTO<Object> leaveChatRoom(@PathVariable(name = "roomId") String roomId,
                                                     @RequestParam(value = "participant", required = true) String participant){
        return chatRoomService.exitChatRoom(roomId, participant);
    }

    @GetMapping("/{roomId}/history")
    ChatRoomResponseDTO<List<MessageDTO>> fetchMessageHistory(@PathVariable(name = "roomId") String roomId,
                                                              @RequestParam(value = "limit", required = false, defaultValue = "50") int limit,
                                                              @RequestParam(value = "offset", required = false, defaultValue = "0") int offset){
        return chatService.fetchMessageHistory(limit, offset, roomId);
    }

    @GetMapping("/active/all")
    public ChatRoomResponseDTO<Set<String>> getActiveChatRoomListing(@RequestParam(value = "participant", required = true) String participant){
        return chatRoomService.getActiveChatRoomListing(participant);
    }
}
