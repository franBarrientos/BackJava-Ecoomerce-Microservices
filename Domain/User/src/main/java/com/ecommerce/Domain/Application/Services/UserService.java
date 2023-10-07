package com.ecommerce.Domain.Application.Services;


import com.ecommerce.Domain.Application.Dtos.CustomerDTO;
import com.ecommerce.Domain.Application.Dtos.UserDTO;
import com.ecommerce.Domain.Application.Exceptions.NotFoundException;
import com.ecommerce.Domain.Application.Mappers.UserDtoMapper;
import com.ecommerce.Domain.Application.Repositories.UserRepository;
import com.ecommerce.Domain.Domain.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jdk.net.ExtendedSocketOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.time.Duration;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final WebClient.Builder webClientBuilder;

    HttpClient client = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(NioChannelOption.of(ExtendedSocketOptions.TCP_KEEPIDLE), 300)
            .option(NioChannelOption.of(ExtendedSocketOptions.TCP_KEEPINTERVAL), 60)
            .option(NioChannelOption.of(ExtendedSocketOptions.TCP_KEEPCOUNT), 8)
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });


    private CustomerDTO getCustomerDTO(Long userId){

        WebClient webClient = this.webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8083/api/v1/customers/user")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjb3JyZW8xQGNvcnJlby5jb20iLCJpYXQiOjE2OTY2NDMxOTIsImV4cCI6MTY5NjcyOTU5Mn0.aaYULYx5eMEmJcjdxQRW-ZgCRKoRQlYu2-f-farvEyI")
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8083/api/v1/customers/user"))
                .build();

        JsonNode block1 = webClient.method(HttpMethod.GET).uri("/"+userId)
                .retrieve().bodyToMono(JsonNode.class).block();


        return new ObjectMapper().convertValue(block1.get("body"), CustomerDTO.class);
    }


    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return this.userRepository
                .findAll(pageable)
                .map(userDtoMapper::toDto)
                .map(c-> {
                    c.setCustomer(this.getCustomerDTO(c.getId()));
                    return c;
                });
    }

    public UserDTO getUserIsActive(Long id) {
         UserDTO user = this.userDtoMapper
                .toDto(this.userRepository
                .findUserIsActive(id)
                .orElseThrow(() -> new NotFoundException("user " + id + " not found")));
         user.setCustomer(this.getCustomerDTO(user.getId()));
        return user;
    }

    public String deleteById(Long id) {
        User user = this.userRepository.findUserIsActive(id)
                .orElseThrow(() -> new NotFoundException("User " + id + " not found"));
        user.setState(false);
        userRepository.save(user);
        return "user " + id + " deleted";
    }


    public UserDTO updateById(long id, UserDTO body) {
        Optional<User> userUpdated = this.userRepository
                .updateById(id, this.userDtoMapper.toDomain(body));

        if (userUpdated.isEmpty()){
            throw new NotFoundException("user " + id + " not found");

        }else {
            return this.userDtoMapper
                    .toDto(userUpdated.get());
        }
    }
}
