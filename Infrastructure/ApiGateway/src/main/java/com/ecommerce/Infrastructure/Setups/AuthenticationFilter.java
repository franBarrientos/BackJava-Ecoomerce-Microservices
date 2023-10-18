package com.ecommerce.Infrastructure.Setups;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilterFactory<AuthenticationFilter.Config> {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final String VALUE = "rolesParameter";

    private final WebClient.Builder webclientBuilder;
    private final EurekaClient eurekaClient;
    private final HttpClient client;


    @Override
    public Config newConfig() {
        return new Config();
    }


    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }


    private String getUrlUsers() {
        InstanceInfo service = this.eurekaClient
                .getApplication("users")
                .getInstances()
                .get(0);
        return service.getHostName() + ":" + service.getPort();
    }

    private List<String> getStringRolesPermitted(String rolesParameter) {
        return List.of(rolesParameter.split(","));
    }


    @Override
    public GatewayFilter apply(Config config) {

        return new OrderedGatewayFilter((exchange, chain) -> {

            log.info("Authenticate Filter executed");


            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing Authorization Header");
            }


            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] parts = authHeader.split(" ");
            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad structure of Authorization Header");
            }

            List<String> rolesPermitted = this.getStringRolesPermitted(config.rolesParameter);

              return this.webclientBuilder.build()
                                    .get()
                                    .uri("http://" + this.getUrlUsers() + "/api/v1/auth/roles")
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + parts[1])
                                    .retrieve()
                                    .bodyToMono(JsonNode.class)
                                    .map(response -> {
                                        ServerHttpRequest request;
                                        if (response != null) {
                                            log.info("See Objects: " + response);
                                            AuthDetails authDetails = new ObjectMapper().convertValue(response, AuthDetails.class);
                                            log.info("Users' Roles :"+authDetails.getRoles());
                                            log.info("Users' Roles :"+authDetails.getRoles());
                                            log.info("Users' Roles :"+authDetails.getRoles());
                                            log.info("Users' Roles :"+authDetails.getRoles());
                                            log.info("Users' Roles :"+authDetails.getRoles());
                                            if (authDetails.getRoles().stream().anyMatch(rolesPermitted::contains)) {
                                                String adminRole = "ROLE_ADMIN";
                                                String isAdmin = authDetails.getRoles().stream().anyMatch(adminRole::contains) ? "true" : "false";
                                                request = exchange.getRequest().mutate().header("userId", authDetails.getUserId().toString()).header("isAdmin", isAdmin).build();

                                            }else {
                                                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access Denied");
                                            }

                                        } else {
                                            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access Denied");
                                        }
                                        return exchange.mutate().request(request).build();
                                    }).onErrorMap(error -> {
                                            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access Denied");
                                    })
                                    .flatMap(chain::filter);

        }, 1);
    }

    public static class Config {
        private String rolesParameter;

        public String getRolesParameter() {
            return rolesParameter;
        }

        public void setRolesParameter(String value) {
            rolesParameter = value;
        }


    }
}


