package com.example.Login.controller;

import com.example.Login.dto.CounterDto;
import com.example.Login.dto.DayWiseCountDto;
import com.example.Login.dto.SearchDto;
import com.example.Login.dto.WebhookApiPayload;
import com.example.Login.enums.Status;
import com.example.Login.services.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

    @GetMapping("/webhook-stats/{table_name}/{timeRate}")
    public CounterDto triggerWebhook(@PathVariable String table_name, @PathVariable String timeRate) {
        return webhookService.getWebhookCountLastMinute(table_name, timeRate);
    }

    @GetMapping("/dayChart/{table_name}")
    public List<DayWiseCountDto> getSalesData(@PathVariable String table_name) {
        return webhookService.getLastDayData(table_name);
    }

    @GetMapping("/search/{table_name}")
    public List<SearchDto> search(@PathVariable String table_name, @RequestParam int page, @RequestParam int size,@RequestParam String productId) {
        return webhookService.getSyncedProductIds(table_name, page, size, productId);
    }

    @GetMapping("/remove/{event_key}")
    public boolean removeWebhook(@PathVariable String event_key) {
        return webhookService.removeWebhook(event_key);
    }
}
