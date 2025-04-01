package com.example.chatroom.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatRoomRequestDTO(@NotNull String name, String owner) {
}
