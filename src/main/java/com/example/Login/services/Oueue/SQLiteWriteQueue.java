package com.example.Login.services.Oueue;

import com.example.Login.dto.ProductInfo;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class SQLiteWriteQueue {
    private final ConcurrentHashMap<String, BlockingQueue<AbstractMap.SimpleEntry<String, List<ProductInfo>>>> queueMap = new ConcurrentHashMap<>();

    public void createQueue(String tableName) {
        queueMap.putIfAbsent(tableName, new LinkedBlockingQueue<>()); // Ensure queue exists
    }

    public void addToQueue(String tableName, List<ProductInfo> productInfo) throws InterruptedException {
        AbstractMap.SimpleEntry<String, List<ProductInfo>> data = new AbstractMap.SimpleEntry<>(tableName, productInfo);
        queueMap.computeIfAbsent(tableName, k -> new LinkedBlockingQueue<>()).put(data);
    }


    public AbstractMap.SimpleEntry<String, List<ProductInfo>> getMessageFromQueue(String tableName) throws InterruptedException  {
        return queueMap.computeIfAbsent(tableName, k -> new LinkedBlockingQueue<>()).take();
    }

    public int getQueueSize(String tableName) {
        try{
            return queueMap.get(tableName).size();
        }catch (Exception ex){
            System.out.println("Error while getting the queue size" + ex.getMessage());
            return 0;
        }
    }
}
