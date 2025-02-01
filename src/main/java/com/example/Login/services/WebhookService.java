package com.example.Login.services;

import com.example.Login.Dao.CommonDao;
import com.example.Login.dto.*;
import com.example.Login.model.Webhook;
import com.example.Login.repo.WebhookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            List<Integer> productIds = payload.getProductId();
            int chunkSize = 5;

            List<List<Integer>> chunks = IntStream.range(0, (productIds.size() + chunkSize - 1) / chunkSize)
                    .mapToObj(i -> productIds.subList(i * chunkSize, Math.min((i + 1) * chunkSize, productIds.size())))
                    .collect(Collectors.toList());

            for (List<Integer> chunk : chunks) {
                long lengthOfChunk = chunk.size();
                String productIdsString = chunk.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
                commonDao.triggerWebhook(payload, productIdsString, lengthOfChunk);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Webhook call failed: " + e.getMessage());
            return false;
        }
    }

    public CounterDto getWebhookCountLastMinute(String tableName, String timeRate) {
        String rate = timeRate.trim().replaceAll("\\D", "");
        String liveRateQuery = timeRate.toLowerCase().contains("m") ? "minutes" : "seconds";
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        return new CounterDto( formatter.format(commonDao.getLiveSyncProductCount(tableName, rate, liveRateQuery)),
                formatter.format(commonDao.getDayProductCountWithStatus(tableName, "PASSED")),
                formatter.format(commonDao.getDayProductCountWithStatus(tableName, "FAILED")));
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