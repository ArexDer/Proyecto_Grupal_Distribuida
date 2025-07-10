package com.app.app_customers.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayRepairConfig {

    @Bean
    public CommandLineRunner repairFlyway(Flyway flyway) {
        return args -> {
            flyway.repair();
            System.out.println("✔ Flyway repair ejecutado.");
        };
    }
}