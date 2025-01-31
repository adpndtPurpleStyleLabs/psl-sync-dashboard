package com.example.Login.dto;

import com.example.Login.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public class WebhookPayload {
    private Status eventStatus;
    private List<Long> productIds;
    private LocalDateTime receivedAt;

    public WebhookPayload() {
        this.receivedAt = LocalDateTime.now();
    }

    public WebhookPayload(Status eventStatus, List<Long> productIds) {
        this.eventStatus = eventStatus;
        this.productIds = productIds;
        this.receivedAt = LocalDateTime.now();
    }

    public Status getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Status eventStatus) {
        this.eventStatus = eventStatus;
    }

    public List<Long> getProductId() {
        return productIds;
    }

    public void setProductId(List<Long> productIds) {
        this.productIds = productIds;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }
}
