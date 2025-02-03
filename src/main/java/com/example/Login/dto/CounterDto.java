package com.example.Login.dto;

public class CounterDto {
    public String liveCounter;
    public String passedCount;
    public String failedCount;
    public String backLogCount;

    public CounterDto(){}

    public CounterDto(String liveCounter, String passedCount, String failedCount, String backLogCount) {
        this.liveCounter = liveCounter;
        this.passedCount = passedCount;
        this.failedCount = failedCount;
        this.backLogCount = backLogCount;
    }

    public String getLiveCounter() {
        return liveCounter;
    }

    public void setLiveCounter(String liveCounter) {
        this.liveCounter = liveCounter;
    }

    public String getPassedCount() {
        return passedCount;
    }

    public void setPassedCount(String passedCount) {
        this.passedCount = passedCount;
    }

    public String getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(String failedCount) {
        this.failedCount = failedCount;
    }

    public String getBackLogCount() {
        return backLogCount;
    }

    public void setBackLogCount(String backLogCount) {
        this.backLogCount = backLogCount;
    }
}