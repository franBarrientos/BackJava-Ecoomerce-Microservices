package com.ecommerce.Infrastructure.Setups;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class GlobalPostFilter{

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {;
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        log.info("Global postfilter executed");
                    }));
        };
    }
}
