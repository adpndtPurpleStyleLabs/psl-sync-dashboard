package com.example.Login.services.QueueWorker;

import com.example.Login.dto.ProductInfo;
import com.example.Login.services.Oueue.SQLiteWriteQueue;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.List;

@Component
public class SQLiteWriteWorker {
    @Autowired
    private SQLiteWriteQueue writeQueue;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void startWorkerThread() {
        Thread worker = new Thread(() -> {
            while (true) {
                try {
                    AbstractMap.SimpleEntry<String, List<ProductInfo>> queueMsg = writeQueue.takeFromQueue();
                    saveToDatabase(queueMsg.getKey(), queueMsg.getValue());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        worker.setDaemon(true);
        worker.start();
    }

    private void saveToDatabase(String tableName, List<ProductInfo> productInfobulk) {
        String sql = "INSERT INTO " + tableName + " (id, jsonData, receivedAt) VALUES (?, ?, ?) " +
                "ON CONFLICT(id) DO UPDATE SET jsonData = excluded.jsonData, receivedAt = excluded.receivedAt;";

        jdbcTemplate.batchUpdate(sql, productInfobulk, productInfobulk.size(), (ps, productInfo) -> {
            ps.setString(1, productInfo.getProductId());
            ps.setString(2, productInfo.getProductDetails());
            ps.setTimestamp(3, Timestamp.valueOf(productInfo.getReceivedAt()));
        });
        System.out.println("Saved to db current queue size is "+ writeQueue.getQueueSize());
    }
}
