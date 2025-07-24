package com.app.app_authors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class AuthorsLifeCycle {

    @Value("${spring.cloud.consul.host:127.0.0.1}")
    private String consulHost;

    @Value("${spring.cloud.consul.port:8500}")
    private Integer consulPort;

    @Value("${server.port}")
    private Integer appPort;

    private final ConsulAutoServiceRegistration serviceRegistration;
    private final ConsulServiceRegistry serviceRegistry;
    private final HealthEndpoint healthEndpoint;

    public AuthorsLifeCycle(ConsulAutoServiceRegistration serviceRegistration,
                            ConsulServiceRegistry serviceRegistry,
                            HealthEndpoint healthEndpoint) {
        this.serviceRegistration = serviceRegistration;
        this.serviceRegistry = serviceRegistry;
        this.healthEndpoint = healthEndpoint;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws UnknownHostException {
        System.out.println("Starting AUTHORS en Consul");

        var ipAddress = InetAddress.getLocalHost();
        System.out.printf("Se registro el servicio: %s:%d%n", ipAddress.getHostAddress(), appPort);

        var healthStatus = healthEndpoint.health();
        System.out.printf("Estado de Health de Authors: %s%n", healthStatus.getStatus());

        serviceRegistration.start();
        System.out.println("AUTHORS se inicio correctamente y se registro en Consull");
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        System.out.println("Shutting down AUTHORS");
        serviceRegistration.stop();
        System.out.println("AUTHORS service saliendo de  Consul");
    }
}