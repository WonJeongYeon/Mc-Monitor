package com.example.mc_monitor.module;

import com.example.mc_monitor.config.McServerState;
import com.example.mc_monitor.model.McServerStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class McServer {

    private final WebClient webClient;

    private final McServerState state;

    private final DiscordBot discordBot;

    public Mono<McServerStatus> getStatus() {
        return webClient.get()
                .uri("/status")
                .retrieve()
                .bodyToMono(McServerStatus.class);
    }



    @Scheduled(fixedDelay = 5000)
    public void update() {
        log.info("[McServer] Updating Server Status...");
        getStatus()
                .doOnNext(state::setState)
                .doOnError(e -> log.error("MC status error", e))
                .subscribe();

        if (state.getStatus() == null) {
            discordBot.updateStatus("\uD83D\uDD34 서버 상태 : 오프라인");
            return;
        }

        if (state.getStatus().isOnline()) {
            discordBot.updateStatus("\uD83D\uDFE2 서버 상태 : 온라인");
        } else {
            discordBot.updateStatus("\uD83D\uDD34 서버 상태 : 오프라인");
        }
    }
}
