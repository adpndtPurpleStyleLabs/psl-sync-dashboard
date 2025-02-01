package com.example.Login.controller;

import com.example.Login.model.Webhook;
import com.example.Login.services.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/webhooks")
public class WebHookController {

    @Autowired
    private WebhookService webhookService;

    @GetMapping("/new")
    public String showWebhookForm(Model model) {
        model.addAttribute("webhook", new Webhook());
        return "webhook-form";
    }

    @PostMapping("/register")
    public String registerWebhook(@ModelAttribute Webhook webhook) {
        webhookService.registerWebhook(webhook.getEventName(), webhook.getEventKey());
        return "redirect:/index";
    }
}
