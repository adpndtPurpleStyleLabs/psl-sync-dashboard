package com.example.Login.config;

import com.example.Login.services.websocketHandler.ChartWebSocketHandler;
import com.example.Login.services.websocketHandler.CounterWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChartWebSocketHandler(), "/chart-updates/{tableName}").setAllowedOrigins("*");
        registry.addHandler(new CounterWebSocketHandler(), "/counter-updates/{tableName}/{timeRate}").setAllowedOrigins("*");
    }
}
