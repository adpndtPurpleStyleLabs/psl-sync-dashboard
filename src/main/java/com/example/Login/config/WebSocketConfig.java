package com.example.Login.config;

import com.example.Login.services.WebhookService;
import com.example.Login.services.websocketHandler.ChartWebSocketHandler;
import com.example.Login.services.websocketHandler.CounterWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebhookService webhookService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChartWebSocketHandler(), "/chart-updates/{tableName}").setAllowedOrigins("*");
        registry.addHandler(new CounterWebSocketHandler(webhookService), "/counter-updates/{tableName}/{timeRate}").setAllowedOrigins("*");
    }
}