package com.example.Login.controller;

import com.example.Login.services.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LoginSontroller {

    @Autowired
    public WebhookService webhookService;

    @GetMapping("/req/login")
    public String login() {
        return "login";
    }

    @GetMapping("/req/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/index")
    public String home(Model model) {
        long count = webhookService.getWebhookCountLastMinute("test_key_2");
        model.addAttribute("webhookCountLastMinute", count);
        return "index";
    }

    @GetMapping("/index/{table_name}")
    public String homeCustom(@PathVariable String table_name, Model model) {
        model.addAttribute("tableName", table_name);
        return "index";
    }

    @GetMapping("/createWebHook")
    public String createWebHook() {
        return "webhook-form";
    }
}