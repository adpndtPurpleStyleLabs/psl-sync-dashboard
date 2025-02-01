package com.example.Login.dto;

public class SearchDto {
    private String receivedAt;
    private String listOfProductIds;
    private String status;

    public SearchDto() {
    }

    public SearchDto(String receiverAt, String listOfProductIds, String status) {
        this.receivedAt = receiverAt;
        this.listOfProductIds = listOfProductIds;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(String receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getListOfProductIds() {
        return listOfProductIds;
    }

    public void setListOfProductIds(String listOfProductIds) {
        this.listOfProductIds = listOfProductIds;
    }
}
