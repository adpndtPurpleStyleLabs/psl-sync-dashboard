package com.example.Login.dto;

public class CounterDto {
    public int liveCounter;
    public int passedCount;
    public int failedCount;

    public CounterDto(){}

    public CounterDto(int liveCounter, int passedCount, int failedCount) {
        this.liveCounter = liveCounter;
        this.passedCount = passedCount;
        this.failedCount = failedCount;
    }

    public int getLiveCounter() {
        return liveCounter;
    }

    public void setLiveCounter(int liveCounter) {
        this.liveCounter = liveCounter;
    }

    public int getPassedCount() {
        return passedCount;
    }

    public void setPassedCount(int passedCount) {
        this.passedCount = passedCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }
}