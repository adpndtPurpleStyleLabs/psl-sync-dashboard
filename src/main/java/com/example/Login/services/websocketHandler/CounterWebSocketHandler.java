package com.example.Login.services.websocketHandler;

import com.example.Login.services.WebhookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.*;

public class CounterWebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<WebSocketSession, ScheduledFuture<?>> sessionTasks = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public final WebhookService webhookService;

    public CounterWebSocketHandler(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Map.Entry<String, String> info = extractLastSegment(session);
        String timeRate = info.getValue();
        String tableName = info.getKey();
        int rate = Integer.valueOf(timeRate.trim().replaceAll("\\D", ""));
//        rate = timeRate.toLowerCase().contains("m") ? rate * 60 : rate;
        rate = timeRate.toLowerCase().contains("m") ? 5 : 1;

        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> {
            try {
                String jsonData = objectMapper.writeValueAsString(webhookService.getWebhookCountLastMinute(tableName.trim(), timeRate.trim()));
                session.sendMessage(new TextMessage(jsonData));
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }, 0, rate, TimeUnit.SECONDS);
        sessionTasks.put(session, task);
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
        if (sessionTasks.containsKey(session)) {
            sessionTasks.get(session).cancel(true);
            sessionTasks.remove(session);
            System.out.println("🔴 WebSocket closed for session.");
        }
    }
}
