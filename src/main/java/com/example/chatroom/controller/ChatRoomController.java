package com.example.chatroom.controller;

import com.example.chatroom.dto.ChatRoomRequestDTO;
import com.example.chatroom.dto.ChatRoomResponseDTO;
import com.example.chatroom.model.ChatRoom;
import com.example.chatroom.service.ChatRoomService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/")
    public ChatRoomResponseDTO<Object> createChatRoom(@RequestBody ChatRoomRequestDTO chatRoomRequestDTO){
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
}
