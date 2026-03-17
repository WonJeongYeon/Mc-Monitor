package com.example.mc_monitor.module;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DiscordBot {

    private final JDA jda;

    @PostConstruct
    public void init() {
        log.info("Discord bot ready: {}", jda.getSelfUser().getName());
        jda.getPresence().setActivity(
                Activity.watching("봇 초기화 중...")
        );
    }

    public void updateStatus(String message) {
        jda.getPresence().setActivity(Activity.watching(message));
    }
}
