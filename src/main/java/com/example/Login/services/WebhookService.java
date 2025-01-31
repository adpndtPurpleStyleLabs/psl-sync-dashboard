package com.example.Login.services;

import com.example.Login.dto.WebhookApiPayload;
import com.example.Login.dto.WebhookPayload;
import com.example.Login.model.Webhook;
import com.example.Login.repo.WebhookRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WebhookService {
    private final WebhookRepository webhookRepository;
    private final RestTemplate restTemplate;
    private final JdbcTemplate jdbcTemplate;

    public WebhookService(WebhookRepository webhookRepository
            , JdbcTemplate jdbcTemplate) {
        this.webhookRepository = webhookRepository;
        this.restTemplate = new RestTemplate();
        this.jdbcTemplate = jdbcTemplate;
    }

    public Webhook registerWebhook(String eventName, String eventKey) {
        Webhook webhook = new Webhook(eventName, eventKey);
        this.createTable(sanitizeTableName(webhook.getEventKey()));
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

            String insertQuery = "INSERT INTO " + sanitizeTableName(payload.getEventKey()) + " (event_status, payload, count) VALUES ('" + payload.getEventStatus() + "', '" + productIdsString + "', " + lengthOfProductIds + ");";
            jdbcTemplate.update(insertQuery);
            return true;
        } catch (Exception e) {
            System.err.println("Webhook call failed: " + e.getMessage());
            return false;
        }
    }

    private String sanitizeTableName(String eventKey) {
        return eventKey.replaceAll("[^a-zA-Z0-9_]", "_").toLowerCase();
    }

    public void createTable(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS "+tableName+" (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    event_status VARCHAR(10) NOT NULL,\n" +
                "    payload TEXT NOT NULL,\n" +
                "    count INTEGER NOT NULL,\n" +
                "    received_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                ");";
        jdbcTemplate.execute(sql);
    }

    public long getWebhookCountLastMinute(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName ;
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        String formattedTime = oneMinuteAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public Map<String, Object> getLastDayData(String tableName) {
        String sql = "SELECT \n" +
                "    strftime('%m/%d', received_at) AS day,\n" +
                "    COUNT(*) AS total_count,\n" +
                "    SUM(COUNT(*)) OVER (ORDER BY strftime('%m/%d', received_at)) AS cumulative_sum\n" +
                "FROM \n" +
                "    "+tableName+"\n" +
                "WHERE \n" +
                "    received_at >= datetime('now', '-6 days')\n" +
                "GROUP BY \n" +
                "    day\n" +
                "ORDER BY \n" +
                "    day ASC";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        Map<String, Integer> salesMap = results.stream()
                .collect(Collectors.toMap(
                        row -> row.get("day").toString(),
                        row -> Integer.parseInt(row.get("total_count").toString())
                ));

        Map<String, Object> response = new HashMap<>();
        List<String> last7Days = getLast7Days();

        response.put("labels", last7Days);
        response.put("sales", last7Days.stream()
                .map(day -> salesMap.getOrDefault(day, 0))
                .collect(Collectors.toList()));

        return response;
    }

    public static List<String> getLast7Days() {
        List<String> last7Days;
        last7Days = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        for (int i = 0; i < 7; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -i);
            last7Days.add(sdf.format(cal.getTime()));
        }
        return last7Days;
    }
}