package com.example.Login.dto;

import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.CopyOnWriteArrayList;

public class CounterWebSocketDto {
    private String tableName;
    private String timeRate;
    private CopyOnWriteArrayList<WebSocketSession> session ;

    public CounterWebSocketDto(){}

    public CounterWebSocketDto(String tableName, String timeRate, CopyOnWriteArrayList<WebSocketSession> session) {
        this.tableName = tableName;
        this.timeRate = timeRate;
        this.session = session;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTimeRate() {
        return timeRate;
    }

    public void setTimeRate(String timeRate) {
        this.timeRate = timeRate;
    }

    public CopyOnWriteArrayList<WebSocketSession> getSession() {
        return session;
    }

    public void setSession(CopyOnWriteArrayList<WebSocketSession> session) {
        this.session = session;
    }
}
