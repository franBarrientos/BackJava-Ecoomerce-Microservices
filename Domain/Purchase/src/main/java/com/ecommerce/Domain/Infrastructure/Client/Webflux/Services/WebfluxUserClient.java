package com.ecommerce.Domain.Infrastructure.Client.Webflux.Services;

import com.ecommerce.Domain.Application.Client.UserClient;
import com.ecommerce.Domain.Application.Dtos.CustomerDTO;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebfluxUserClient implements UserClient {
    private final WebClient.Builder webClientBuilder;
    private final HttpClient client;
    private final EurekaClient eurekaClient;

    @Override
    public CustomerDTO getCustomerDTO(Long customerId){

        WebClient webClient = this.webClientBuilder.clientConnector(new ReactorClientHttpConnector(this.client))
                .baseUrl("http://"+this.getUrlUser()+"/api/v1/customers")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://"+this.getUrlUser()+"/api/v1/customers"))
                .build();

        JsonNode block1 = webClient.method(HttpMethod.GET).uri("/"+customerId)
                .retrieve().bodyToMono(JsonNode.class).block();


        return new ObjectMapper().convertValue(block1.get("body"), CustomerDTO.class);
    }

    @Override
    public List<CustomerDTO> searchCustomers(Integer dni, String firstName, String lastName) {
        String params =
                "?"+(dni != null ? "dni="+dni : "")+(firstName != null ? "&firstName="+firstName : "")+(lastName != null ? "&lastName="+lastName : "");
        WebClient webClient = this.webClientBuilder.clientConnector(new ReactorClientHttpConnector(this.client))
                .baseUrl("http://"+this.getUrlUser()+"/api/v1/customers/search"+params)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url",
                        "http://"+this.getUrlUser()+"/api/v1/customers"+params))
                .build();

        JsonNode jsonResponse = webClient.method(HttpMethod.GET)
                .retrieve().bodyToMono(JsonNode.class).block();

        if (jsonResponse != null && jsonResponse.has("body") && jsonResponse.get("body").isArray()) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(jsonResponse.get("body"), new TypeReference<List<CustomerDTO>>() {});
        }

        return Collections.emptyList(); 
    }


    private String getUrlUser(){
        InstanceInfo service = this.eurekaClient
                .getApplication("users")
                .getInstances()
                .get(0);

        return service.getHostName()+":"+service.getPort();
    }


}
