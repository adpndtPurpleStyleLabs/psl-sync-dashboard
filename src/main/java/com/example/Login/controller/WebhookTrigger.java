package com.example.Login.controller;

import com.example.Login.dto.WebhookApiPayload;
import com.example.Login.services.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trigger")
public class WebhookTrigger {

    @Autowired
    private WebhookService webhookService;

    @PostMapping("/{eventKey}")
    public boolean triggerWebhook(@PathVariable String eventKey, @RequestBody WebhookApiPayload payload) {
        boolean success = webhookService.triggerWebhook(eventKey, payload);
        return true;
    }

    @GetMapping("/webhook-stats/{table_name}")
    public String triggerWebhook(@PathVariable String table_name) {
        return String.valueOf(webhookService.getWebhookCountLastMinute(table_name));
    }

    @GetMapping("/dayChart/{table_name}")
    public Map<String, Object> getSalesData(@PathVariable String table_name) {
        return webhookService.getLastDayData(table_name);
    }
}
