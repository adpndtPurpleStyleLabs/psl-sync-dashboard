package com.example.Login.services;

import com.example.Login.Dao.CommonDao;
import com.example.Login.dto.*;
import com.example.Login.model.Webhook;
import com.example.Login.repo.WebhookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WebhookService {
    @Autowired
    private WebhookRepository webhookRepository;

    @Autowired
    private CommonDao commonDao;

    public Webhook registerWebhook(String eventName, String eventKey) {
        Webhook webhook = new Webhook(eventName, eventKey);
        commonDao.createATable(webhook.getEventKey());
        return webhookRepository.save(webhook);
    }

    public List<Webhook> getAllWebhooks() {
        return webhookRepository.findAll();
    }

    public boolean triggerWebhook(WebhookApiPayload payload) {
        try {
            long lengthOfProductIds = payload.getProductId().stream().count();
            String productIdsString = payload.getProductId().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            commonDao.triggerWebhook(payload, productIdsString, lengthOfProductIds);
            return true;
        } catch (Exception e) {
            System.err.println("Webhook call failed: " + e.getMessage());
            return false;
        }
    }

    public CounterDto getWebhookCountLastMinute(String tableName, String timeRate) {
        String rate = timeRate.trim().replaceAll("\\D", "");
        String liveRateQuery = timeRate.toLowerCase().contains("m") ? "minutes" : "seconds";
        Integer liveCount = commonDao.getLiveSyncProductCount(tableName, rate, liveRateQuery);
        Integer passedCount = commonDao.getDayProductCountWithStatus(tableName, "PASSED");
        Integer failedCount = commonDao.getDayProductCountWithStatus(tableName, "FAILED");
        return new CounterDto(liveCount, passedCount, failedCount);
    }

    public List<DayWiseCountDto> getLastDayData(String tableName) {
        return commonDao.getWeekData(tableName);
    }

    public List<SearchDto> getSyncedProductIds(String tableName , int page, int size, String productId) {
        if (productId == null || productId == "") {
            return commonDao.getSyncedProductIds(tableName, page, size);
        }
        return commonDao.getSyncedProductIdsWithSearch(tableName, page, size, productId);
    }

    public boolean removeWebhook(String eventKey) {
        try{
            commonDao.deleteWebhook(eventKey);
            return true;
        } catch (Exception ex){
            return false;
        }
    }
}