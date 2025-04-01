package com.example.chatroom.dto;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatRoomResponseDTO<T>(String roomId, String message, String status, T data) {
}
