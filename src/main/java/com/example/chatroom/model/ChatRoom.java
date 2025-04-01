package com.example.chatroom.model;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {
    private String name; // used as key for hash
    private String owner;
    private long timestamp;
}
