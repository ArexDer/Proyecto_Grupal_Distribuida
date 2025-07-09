package com.app.app_books;

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
public class BooksLifeCicle{

    @Value("${spring.cloud.consul.host:127.0.0.1}")
    private String consulHost;

    @Value("${spring.cloud.consul.port:8500}")
    private Integer consulPort;

    @Value("${server.port}")
    private Integer appPort;

    private final ConsulAutoServiceRegistration serviceRegistration;
    private final ConsulServiceRegistry serviceRegistry;

    public BooksLifeCicle(ConsulAutoServiceRegistration serviceRegistration,
                          ConsulServiceRegistry serviceRegistry) {
        this.serviceRegistration = serviceRegistration;
        this.serviceRegistry = serviceRegistry;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws UnknownHostException {
        System.out.println("Starting BOOKS service registration with Consul");

        var ipAddress = InetAddress.getLocalHost();
        System.out.printf("Registering service at %s:%d%n", ipAddress.getHostAddress(), appPort);

        // Spring Cloud Consul se encarga del registro usando application.properties
        serviceRegistration.start();
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        System.out.println("Shutting down BOOKS service instances");

        // Spring Cloud Consul se encarga de la desregistración
        serviceRegistration.stop();
    }

}
