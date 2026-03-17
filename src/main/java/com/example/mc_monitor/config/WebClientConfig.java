package com.example.mc_monitor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${mc.server.ip}")
    private String ip;

    @Value("${mc.server.port}")
    private String port;

    @Bean
    public WebClient mcWebClient() {
        return WebClient.builder()
                .baseUrl("http://" + ip + ":" + port)
                .build();
    }

}
