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
    public boolean triggerWebhook(String eventKey, WebhookApiPayload payload) {
        Optional<Webhook> webhookOpt = webhookRepository.findAll()
                .stream()
                .filter(webhook -> webhook.getEventKey().equals(eventKey))
                .findFirst();

        if (webhookOpt.isPresent()) {
            Webhook webhook = webhookOpt.get();
            try {
                WebhookPayload webhookPayload = new WebhookPayload(
                        payload.getEventType(),
                        payload.getPayload()
                );

                storeWebhookData(sanitizeTableName(webhook.getEventKey()), webhookPayload);
                return true;
            } catch (Exception e) {
                System.err.println("Webhook call failed: " + e.getMessage());
                return false;
            }
        }
        return false;
    }
    private String sanitizeTableName(String eventKey) {
        return eventKey.replaceAll("[^a-zA-Z0-9_]", "_").toLowerCase();
    }
    public void storeWebhookData(String eventKey, WebhookPayload payload) {
        String tableName = sanitizeTableName(eventKey);
        String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String sql = "INSERT INTO  " + tableName + "  (event_type, payload, received_at) " +
                "VALUES ('"+payload.getEventType()+"', '"+payload.getPayload()+"','"+formattedTime+"');";
        jdbcTemplate.update(sql);
    }
    public void createTable(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS "+tableName+" (\n" +
                "    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    event_type VARCHAR(255) NOT NULL,\n" +
                "    payload TEXT NOT NULL,\n" +
                "    received_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n" +
                ");\n";
        jdbcTemplate.execute(sql);
    }

    public long getWebhookCountLastMinute(String tableName) {
        String sql = "SELECT COUNT(*) FROM "+tableName+" WHERE received_at >= ?";
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        String formattedTime = oneMinuteAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return jdbcTemplate.queryForObject(sql, new Object[]{formattedTime}, Long.class);
    }

    public  Map<String, Object> getLastDayData(String tableName){
        String sql = "SELECT \n" +
                "    strftime('%m/%d', received_at) AS day, \n" +
                "    COUNT(*) AS total_count \n" +
                "FROM "+tableName+" \n" +
                "WHERE received_at >= datetime('now', '-6 days')\n" +
                "GROUP BY day\n" +
                "ORDER BY day ASC;\n";

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