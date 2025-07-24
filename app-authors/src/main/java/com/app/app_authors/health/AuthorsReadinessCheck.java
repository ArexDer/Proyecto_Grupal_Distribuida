package com.app.app_authors.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component("authorsReadiness")
public class AuthorsReadinessCheck implements HealthIndicator {

    private final DataSource dataSource;

    public AuthorsReadinessCheck(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try {
            checkDatabaseConnection();
            return Health.up()
                    .withDetail("database", "available")
                    .withDetail("database-url", "jdbc:postgresql://localhost:5433/distribuidaG")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("database", "unavailable")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }

    private void checkDatabaseConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute("SELECT 1");
        }
    }
}