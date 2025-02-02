package com.example.Login.dto;

import java.time.LocalDateTime;

public class ProductInfo {
    private String productId;
    private String productDetails;
    private LocalDateTime receivedAt;

    public ProductInfo(){}

    public ProductInfo(String productId, String productDetails, LocalDateTime receivedAt) {
        this.productId = productId;
        this.productDetails = productDetails;
        this.receivedAt = receivedAt;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }
}