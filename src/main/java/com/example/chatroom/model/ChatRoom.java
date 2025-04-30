package com.example.chatroom.model;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {
    private String name;
    private String owner;
    private String description;
    private long timestamp;
}
