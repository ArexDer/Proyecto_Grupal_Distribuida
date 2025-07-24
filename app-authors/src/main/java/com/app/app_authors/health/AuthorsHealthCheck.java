package com.app.app_authors.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("authorsLiveness")
public class AuthorsHealthCheck implements HealthIndicator {

    @Override
    public Health health() {
        return Health.up()
                .withDetail("app-authores-health", "UP ->Esta ok")
                .build();
    }
}