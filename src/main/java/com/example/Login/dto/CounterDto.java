package com.example.Login.dto;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CounterDto {
    public String liveCounter;
    public String passedCount;
    public String failedCount;

    public CounterDto(){}

    public CounterDto(String liveCounter, String passedCount, String failedCount) {
        this.liveCounter = liveCounter;
        this.passedCount = passedCount;
        this.failedCount = failedCount;
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
}