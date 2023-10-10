package com.ecommerce.Domain.Infrastructure.Client.Webflux.Config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class EurekaClient {
    @Bean
    @LoadBalanced
    WebClient.Builder loadBalancedWebClientBuilder() {

        return WebClient.builder();

    }
}
