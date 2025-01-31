package com.example.Login.dto;

import com.example.Login.enums.Status;

import java.util.ArrayList;
import java.util.List;

public class WebhookApiPayload {
    private String eventKey;
    private Status eventStatus;
    private ArrayList<Integer> productIds;

    public WebhookApiPayload() {
    }

    public WebhookApiPayload(String eventKey, Status eventStatus, ArrayList<Integer> productIds) {
        this.eventKey = eventKey;
        this.eventStatus = eventStatus;
        this.productIds = productIds;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public Status getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Status eventStatus) {
        this.eventStatus = eventStatus;
    }

    public List<Integer> getProductId() {
        return productIds;
    }

    public void setProductId(ArrayList<Integer> productIds) {
        this.productIds = productIds;
    }
}
