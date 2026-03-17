package com.example.mc_monitor.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordBotConfig {

    @Value("${discord.token}")
    private String token;

    @Bean
    public JDA jda() throws Exception {
        return JDABuilder.createDefault(token)
                .build()
                .awaitReady();
    }
}
