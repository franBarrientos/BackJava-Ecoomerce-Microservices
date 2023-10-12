package com.ecommerce.Domain.Infrastructure.Client.Webflux.Services;

import com.ecommerce.Domain.Application.Client.ProductClient;
import com.ecommerce.Domain.Application.Dtos.ProductDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class WebfluxProductClient implements ProductClient {

    private final WebClient.Builder webClientBuilder;
    private final HttpClient client;
    private final EurekaClient eurekaClient;

    @Override
    public ProductDTO getProductDTO(Long productId){


        WebClient webClient = this.webClientBuilder.clientConnector(new ReactorClientHttpConnector(this.client))
                .baseUrl("http://"+this.getUrlProducts()+"/api/v1/products")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://"+this.getUrlProducts()+"/api/v1/products"))
                .build();

        JsonNode block1 = webClient.method(HttpMethod.GET).uri("/"+productId)
                .retrieve().bodyToMono(JsonNode.class).block();


        return new ObjectMapper().convertValue(block1.get("body"), ProductDTO.class);
    }

    private String getUrlProducts(){
        InstanceInfo service = this.eurekaClient
                .getApplication("products")
                .getInstances()
                .get(0);
        return service.getHostName()+":"+service.getPort();
    }



}
