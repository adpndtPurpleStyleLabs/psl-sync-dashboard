package com.example.Login.services.Oueue;

import com.example.Login.dto.ProductInfo;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class SQLiteWriteQueue {
    private final BlockingQueue<AbstractMap.SimpleEntry<String, List<ProductInfo>>> queue = new LinkedBlockingQueue<>();

    public void addToQueue(String tableName, List<ProductInfo> productInfo) {
        try {
            queue.put(new AbstractMap.SimpleEntry<>(tableName, productInfo));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public AbstractMap.SimpleEntry<String, List<ProductInfo>> takeFromQueue() throws InterruptedException {
        return queue.take();
    }

    public int getQueueSize() {
        return queue.size();
    }
}
