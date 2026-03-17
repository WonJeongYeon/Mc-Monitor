package com.example.mc_monitor.module;

import com.example.mc_monitor.config.McServerState;
import com.example.mc_monitor.model.McServerStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class McServerManager {

    private final WebClient webClient;

    private final McServerState state;

    public Mono<Void> fetchStatus() {
        return webClient.get()
                .uri("/status")
                .retrieve()
                .bodyToMono(McServerStatus.class)
                .timeout(Duration.ofSeconds(3))
                .doOnNext(state::setState)
                .doOnError(e -> {
                    log.info("[McServerManager] Server status fetch failed. Server status will be OFFLINE.");
                    state.initState();
                })
                .onErrorComplete()
                .then();
    }

}
