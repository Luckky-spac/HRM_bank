// src/main/java/com/example/demo/dto/Message.java
package com.hrm.temp.dto;

public class Message {
    private String message;
    private String token;
    private String userId;
    private String username;

    // เพิ่มบรรทัดนี้ 2 บรรทัด → แดงหายทันที!
    public Message() {}
    public Message

(String message) {
        this.message = message;
    }

    // Getters & Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}