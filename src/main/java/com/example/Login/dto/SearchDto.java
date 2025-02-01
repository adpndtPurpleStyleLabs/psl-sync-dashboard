package com.example.Login.dto;

public class SearchDto {
    private String receivedAt;
    private String listOfProductIds;

    public SearchDto(){}

    public SearchDto(String receiverAt, String listOfProductIds) {
        this.receivedAt = receiverAt;
        this.listOfProductIds = listOfProductIds;
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
