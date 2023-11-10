package com.is1di.courses.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private LocalDateTime timestamp;
    private String message;

    public Message(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
