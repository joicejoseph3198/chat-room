package com.example.chatroom.controller;

import com.example.chatroom.dto.ChatRoomRequestDTO;
import com.example.chatroom.dto.ChatRoomResponseDTO;
import com.example.chatroom.dto.MessageDTO;
import com.example.chatroom.model.ChatRoom;
import com.example.chatroom.service.ChatRoomService;
import com.example.chatroom.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/chatroom")
@Tag(name = "Chat Room APIs")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    public ChatRoomController(ChatRoomService chatRoomService, ChatService chatService) {
        this.chatRoomService = chatRoomService;
        this.chatService = chatService;
    }

    @PostMapping("/")
    @Operation(summary = "Create a chat room", description = "Creates a chat room as per the request")
    public ChatRoomResponseDTO<Object> createChatRoom(@Valid @RequestBody ChatRoomRequestDTO chatRoomRequestDTO){
        return chatRoomService.createChatRoom(chatRoomRequestDTO);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all chat rooms", description = "Returns all active chat rooms currently present")
    public ChatRoomResponseDTO<List<ChatRoom>> getChatRoomListing(){
        return chatRoomService.getChatRoomListing();
    }

    @DeleteMapping("/{roomId}")
    @Operation(summary = "Delete a chat room", description = "Deletes the chatroom associated with the given id")
    public ChatRoomResponseDTO<Object> terminateChatRoom(@PathVariable(name = "roomId") String roomId){
        return chatRoomService.deleteChatRoom(roomId);
    }

    @PostMapping("/{roomId}/join")
    @Operation(summary = "Join chat room", description = "Adds the participant to the group of active members of the chat room")
    public ChatRoomResponseDTO<Object> joinChatRoom(@PathVariable(name = "roomId") String roomId,
                                                    @RequestParam(value = "participant", required = true) String participant){
        return chatRoomService.joinChatRoom(roomId, participant);
    }

    @PostMapping("/{roomId}/exit")
    @Operation(summary = "Join chat room", description = "Removes the participant from the group of active members of the chat room")
    public ChatRoomResponseDTO<Object> leaveChatRoom(@PathVariable(name = "roomId") String roomId,
                                                     @RequestParam(value = "participant", required = true) String participant){
        return chatRoomService.exitChatRoom(roomId, participant);
    }

    @GetMapping("/{roomId}/history")
    @Operation(summary = "Fetch the chat history", description = "Fetches the chat history for the give chat room as per the limit and offset")
    ChatRoomResponseDTO<List<MessageDTO>> fetchMessageHistory(@PathVariable(name = "roomId") String roomId,
                                                              @RequestParam(value = "limit", required = false, defaultValue = "50") int limit,
                                                              @RequestParam(value = "offset", required = false, defaultValue = "0") int offset){
        return chatService.fetchMessageHistory(limit, offset, roomId);
    }

    @GetMapping("/active/all")
    @Operation(summary = "Get all active rooms", description = "Returns all active chat rooms the user/participant is currently part of")
    public ChatRoomResponseDTO<Set<String>> getActiveChatRoomListing(@RequestParam(value = "participant", required = true) String participant){
        return chatRoomService.getActiveChatRoomListing(participant);
    }
}
