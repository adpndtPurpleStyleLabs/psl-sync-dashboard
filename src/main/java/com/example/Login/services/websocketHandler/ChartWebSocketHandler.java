package com.example.Login.services.websocketHandler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChartWebSocketHandler extends TextWebSocketHandler {
    private static final ConcurrentHashMap<String, CopyOnWriteArrayList<WebSocketSession>> sessionMap = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String lastSegment = extractLastSegment(session);
        sessionMap.putIfAbsent(lastSegment, new CopyOnWriteArrayList<>());
        sessionMap.get(lastSegment).add(session);
    }
    private String extractLastSegment(WebSocketSession session) {
        String uri = session.getUri().getPath();
        String[] segments = uri.split("/");
        return segments[segments.length - 1];
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String lastSegment = extractLastSegment(session);
        sessionMap.get(lastSegment).remove(session);
        if (sessionMap.get(lastSegment).isEmpty()) {
            sessionMap.remove(lastSegment);
        }
    }

    public static void sendChartData(Object jsonData, String tableName) throws IOException {
        if (sessionMap.containsKey(tableName)) {
            for (WebSocketSession session : sessionMap.get(tableName)) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(jsonData)));
                }
            }
        }
    }
}