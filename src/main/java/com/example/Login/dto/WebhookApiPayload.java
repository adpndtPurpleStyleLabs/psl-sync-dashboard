package com.example.Login.dto;

import com.example.Login.enums.Status;

import java.util.ArrayList;
import java.util.List;

public class WebhookApiPayload {
    private String eventKey;
    private Status eventStatus;
    private List<ProductInfo> productInfo;
    private List<String> productIds;

    public WebhookApiPayload() {
    }

    public WebhookApiPayload(String eventKey, Status eventStatus, ArrayList<ProductInfo> productInfo, List<String> productIds) {
        this.eventKey = eventKey;
        this.eventStatus = eventStatus;
        this.productInfo = productInfo;
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

    public List<ProductInfo> getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(List<ProductInfo> productInfo) {
        this.productInfo = productInfo;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }
}