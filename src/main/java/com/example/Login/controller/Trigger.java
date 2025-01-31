package com.example.Login.controller;

import com.example.Login.dto.WebhookApiPayload;
import com.example.Login.enums.Status;
import com.example.Login.services.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/trigger")
public class Trigger {

    @Autowired
    private WebhookService webhookService;

    @PostMapping("/a")
    public boolean triggerWebhook(@RequestBody Map<String, Object> payload) {
        WebhookApiPayload webhookApiPayload = new WebhookApiPayload();
        webhookApiPayload.setEventKey(payload.get("eventKey").toString());
        webhookApiPayload.setEventStatus(Status.valueOf(payload.get("eventStatus").toString()));
        webhookApiPayload.setProductId((ArrayList<Integer>) payload.get("productIds"));
        return webhookService.triggerWebhook(webhookApiPayload);
    }

    @GetMapping("/webhook-stats/{table_name}")
    public String triggerWebhook(@PathVariable String table_name) {
        return String.valueOf(webhookService.getWebhookCountLastMinute(table_name));
    }

    @GetMapping("/dayChart/{table_name}")
    public Map<String, Object>  getSalesData(@PathVariable String table_name) {
        return webhookService .getLastDayData(table_name);
    }
}
