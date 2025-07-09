package com.app.app_books.config;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Configuration
@LoadBalancerClient(name = "authors-service", configuration = WebClientConfig.LoadBalancerConfiguration.class)
public class WebClientConfig {

    @LoadBalanced
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    public static class LoadBalancerConfiguration {
        @Bean
        public ServiceInstanceListSupplier serviceInstanceListSupplier() {
            return new ServiceInstanceListSupplier() {
                @Override
                public String getServiceId() {
                    return "authors-service";
                }

                @Override
                public Flux<List<ServiceInstance>> get() {
                    return Flux.just(List.of(
                            new DefaultServiceInstance("authors-1", "authors-service",
                                    "localhost", 8080, false)
                    ));
                }
            };
        }
    }
}