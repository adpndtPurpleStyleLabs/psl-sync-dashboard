package com.example.Login.services.schedulers;

import com.example.Login.model.Webhook;
import com.example.Login.repo.WebhookRepository;
import com.example.Login.services.WebhookService;
import com.example.Login.services.websocketHandler.ChartWebSocketHandler;
import com.example.Login.services.websocketHandler.CounterWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChartDataScheduler {

    @Autowired
    public WebhookService webhookService;

    @Autowired
    public WebhookRepository webhookRepository;

    @Scheduled(fixedRate = 2000)
    public void sendChartData()  {
        List<Webhook> webhooks  = webhookRepository.findAll();
        webhooks.stream().forEach(x -> {
            try{
                String tableName = x.getEventKey();
                ChartWebSocketHandler.sendChartData(webhookService.getLastDayData(tableName), tableName);
            } catch (Exception ex){
                System.out.println(ex.getMessage()+  ex.getStackTrace());
            }
        });
    }
}
