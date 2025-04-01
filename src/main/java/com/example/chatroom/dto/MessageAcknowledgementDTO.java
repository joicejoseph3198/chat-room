package com.example.chatroom.dto;



public record MessageAcknowledgementDTO(String participant, String message, String status, Long timestamp) {
}
