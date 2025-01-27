package com.example.Login.repo;

import com.example.Login.model.Webhook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface WebhookRepository extends JpaRepository<Webhook, Long> {

}
