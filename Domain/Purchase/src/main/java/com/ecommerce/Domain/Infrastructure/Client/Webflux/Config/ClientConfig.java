package com.ecommerce.Domain.Infrastructure.Client.Webflux.Config;

import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jdk.net.ExtendedSocketOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {

    @Bean
    public HttpClient httpClient(){
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(NioChannelOption.of(ExtendedSocketOptions.TCP_KEEPIDLE), 300)
                .option(NioChannelOption.of(ExtendedSocketOptions.TCP_KEEPINTERVAL), 60)
                .option(NioChannelOption.of(ExtendedSocketOptions.TCP_KEEPCOUNT), 8)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(60000, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(60000, TimeUnit.MILLISECONDS));
                });
    }
}
