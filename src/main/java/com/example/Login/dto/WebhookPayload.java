package com.example.Login.dto;

import java.time.LocalDateTime;

public class WebhookPayload {
    private String eventType;
    private String payload;
    private LocalDateTime receivedAt;

    public WebhookPayload() {
        this.receivedAt = LocalDateTime.now();
    }

    public WebhookPayload(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
        this.receivedAt = LocalDateTime.now();
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }
}
