package com.example.Login.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

@Entity
@Table(name = "dashboards")
public class Dashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "dashboard_name")
    private String dashboardName;

    @Column(name = "webhook_key")
    private String webhookKey;

    public Dashboard() {}
    public Dashboard(String dashboardName, String webhookKey) {
        this.dashboardName = dashboardName;
        this.webhookKey = webhookKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public String getWebhookKey() {
        return webhookKey;
    }

    public void setWebhookKey(String webhookKey) {
        this.webhookKey = webhookKey;
    }
}