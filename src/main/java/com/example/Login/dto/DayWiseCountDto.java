package com.example.Login.dto;

public class DayWiseCountDto {
    private String day;
    private int cumulativePassed;
    private int cumulativeFailed;

    public DayWiseCountDto(){}

    public DayWiseCountDto(String day, int cumulativePassed, int cumulativeFailed) {
        this.day = day;
        this.cumulativePassed = cumulativePassed;
        this.cumulativeFailed = cumulativeFailed;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getCumulativePassed() {
        return cumulativePassed;
    }

    public void setCumulativePassed(int cumulativePassed) {
        this.cumulativePassed = cumulativePassed;
    }

    public int getCumulativeFailed() {
        return cumulativeFailed;
    }

    public void setCumulativeFailed(int cumulativeFailed) {
        this.cumulativeFailed = cumulativeFailed;
    }
}
