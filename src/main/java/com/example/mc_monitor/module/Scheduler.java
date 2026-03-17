package com.example.mc_monitor.module;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
@Slf4j
public class Scheduler {

    private final McServerManager mcServerManager;
    private final DiscordBotManager discordBotManager;

    @Scheduled(fixedDelayString = "${scheduler.delay.fetchServerStatus}")
    public void syncServerStatus() {
        log.info("[Scheduler] Mc Server Status Fetch Start...");
        mcServerManager.fetchStatus()
                .then(Mono.defer(discordBotManager::syncBotStatus))
                .subscribe();
    }
}
