package com.example.Login.controller;

import com.example.Login.model.Dashboard;
import com.example.Login.model.Webhook;
import com.example.Login.repo.DashboardRepository;
import com.example.Login.services.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/webhooks")
public class WebHookController {

    @Autowired
    private WebhookService webhookService;

    @Autowired
    private DashboardRepository dashboardRepository;

    @GetMapping("/new")
    public String showWebhookForm(Model model) {
        model.addAttribute("webhook", new Webhook());
        return "webhook-form";
    }

    @PostMapping("/register")
    public String registerWebhook(@ModelAttribute Webhook webhook) {
        dashboardRepository.save(new Dashboard(webhook.getEventName(), webhook.getEventKey().toLowerCase()));
        webhookService.registerWebhook(webhook.getEventName(), webhook.getEventKey());
        return "redirect:/index";
    }

    @GetMapping
    public String listWebhooks(Model model) {
        List<Webhook> webhooks = webhookService.getAllWebhooks();
        model.addAttribute("webhooks", webhooks);
        return "webhook-list";
    }
}
