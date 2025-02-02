package com.example.Login.services;

import com.example.Login.Dao.CommonDao;
import com.example.Login.dto.*;
import com.example.Login.model.Webhook;
import com.example.Login.repo.WebhookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        commonDao.createAPidTable(webhook.getEventKey());
        return webhookRepository.save(webhook);
    }

    public List<Webhook> getAllWebhooks() {
        return webhookRepository.findAll();
    }

    public boolean triggerWebhook(WebhookApiPayload payload) {
        try {
            int chunkSize = 5;
            List<List<ProductInfo>> chunks = chunkList(payload.getProductInfo(), chunkSize);
            List<String> aa = new ArrayList<>();
            for (List<ProductInfo> chunk : chunks) {
                List<String> a = new ArrayList<>();
                for (ProductInfo product : chunk) {
                    a.add(product.getProductId());
                }
                aa.add(String.join(",", a));
                commonDao.batchSaveProductDetails(payload.getEventKey().trim()+"_pid",chunk);
            }
            commonDao.batchSaveProductIds(payload, aa);
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

    public static <T> List<List<T>> chunkList(List<T> list, int chunkSize) {
        List<List<T>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += chunkSize) {
            chunks.add(list.subList(i, Math.min(list.size(), i + chunkSize)));
        }
        return chunks;
    }

    public String getProductDetails(String tableName, String productId) {
        return commonDao.fetchProductDetails(tableName, productId);
    }
}