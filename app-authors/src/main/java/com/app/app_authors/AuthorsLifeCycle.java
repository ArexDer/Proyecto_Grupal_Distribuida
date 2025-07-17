package com.app.app_authors;

import org.springframework.beans.factory.annotation.Value;
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

    public AuthorsLifeCycle(ConsulAutoServiceRegistration serviceRegistration,
                            ConsulServiceRegistry serviceRegistry) {
        this.serviceRegistration = serviceRegistration;
        this.serviceRegistry = serviceRegistry;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws UnknownHostException {
        System.out.println("Starting AUTHORS service registration with Consul");

        var ipAddress = InetAddress.getLocalHost();
        System.out.printf("Registering service at %s:%d%n", ipAddress.getHostAddress(), appPort);

        // Spring Cloud Consul maneja automáticamente el registro del servicio
        // usando la configuración de application.properties/yml
        serviceRegistration.start();
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        System.out.println("Shutting down AUTHORS service instances");

        // Spring Cloud Consul maneja automáticamente la desregistración con todo igual revisar en caso de que nos de porblemsa
        serviceRegistration.stop();
    }
}