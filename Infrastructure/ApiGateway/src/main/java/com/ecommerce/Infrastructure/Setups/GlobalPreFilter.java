package com.ecommerce.Infrastructure.Setups;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalPreFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Global prefilter executed");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE; // Orden más bajo para ejecutar antes que otros filtros
    }
}
