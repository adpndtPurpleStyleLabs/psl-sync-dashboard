package com.example.Login.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;

@Entity
@Table(name = "webhooks")
public class Webhook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String eventName;
    private String eventKey;
    private LocalDateTime createdAt;

    public Webhook() {
        this.createdAt = LocalDateTime.now();
    }

    public Webhook(String eventName, String eventKey) {
        this.eventName = eventName;
        this.eventKey = eventKey;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
