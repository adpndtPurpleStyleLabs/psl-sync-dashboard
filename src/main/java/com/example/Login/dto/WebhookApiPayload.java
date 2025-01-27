package com.example.Login.dto;

public class WebhookApiPayload {
    private String eventType;
    private String payload;

    public WebhookApiPayload() {
    }

    public WebhookApiPayload(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
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
}
