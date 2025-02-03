package com.example.Login.services.websocketHandler;

import com.example.Login.dto.CounterWebSocketDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CounterWebSocketHandler extends TextWebSocketHandler {
    private static final ConcurrentHashMap<String, CounterWebSocketDto> sessionMap = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Map.Entry<String, String> info = extractLastSegment(session);
        String timeRate = info.getValue();
        String tableName = info.getKey();

        sessionMap.putIfAbsent(tableName,
                new CounterWebSocketDto(
                        tableName,
                        timeRate,
                        new CopyOnWriteArrayList<>()
                ));
        CounterWebSocketDto counterWebSocketDto = sessionMap.get(tableName);
        counterWebSocketDto.getSession().add(session);
    }

    private Map.Entry<String, String> extractLastSegment(WebSocketSession session) {
        String uri = session.getUri().getPath();
        String[] segments = uri.split("/");
        String timeRate = segments[segments.length - 1];
        String tableName = segments[segments.length - 2];
       return new AbstractMap.SimpleEntry<>(tableName, timeRate);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Map.Entry<String, String> info = extractLastSegment(session);
        String timeRate = info.getValue();
        String tableName = info.getKey();
        CounterWebSocketDto counterWebSocketDto = sessionMap.get(tableName);
        counterWebSocketDto.getSession().remove(session);
    }

    public static void sendCounterData(Object jsonData, String tableName) throws IOException {
        if (sessionMap.containsKey(tableName)) {
            for (WebSocketSession session : sessionMap.get(tableName).getSession()) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(jsonData)));
                }
            }
        }
    }
}
