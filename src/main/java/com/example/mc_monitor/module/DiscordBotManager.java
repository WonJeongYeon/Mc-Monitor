package com.example.mc_monitor.module;

import com.example.mc_monitor.config.McServerState;
import com.example.mc_monitor.discord.command.CommandManager;
import com.example.mc_monitor.discord.command.CommandType;
import com.example.mc_monitor.discord.command.impl.ServerCommand;
import com.example.mc_monitor.model.McServerStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DiscordBotManager {

    private final JDA jda;

    private final McServerState state;

    @PostConstruct
    public void init() {
        log.info("Discord bot ready: {}", jda.getSelfUser().getName());
        updateStatus("봇 초기화 중...");
    }

    public Mono<Void> syncBotStatus() {
        McServerStatus status = state.status();
        if (status == null) {
            log.error("[DiscordBot] McServerState is null.");
            return Mono.fromRunnable(() ->
                    updateStatus("\uD83D\uDD34 서버 상태 : 오프라인")
            );
        }

        if (status.isOnline()) {
            log.info("[DiscordBotManager] Mc Server is online. Setting Bot Status...");
            return Mono.fromRunnable(() ->
                    updateStatus("\uD83D\uDFE2 서버 상태 : 온라인("
                            + status.getPlayers() + "/" + status.getMaxPlayers() + ")")
            );
        }

        log.info("[DiscordBotManager] Mc Server is offline. Setting Bot Status...");
        return Mono.fromRunnable(() ->
                updateStatus("\uD83D\uDD34 서버 상태 : 오프라인")
        );
    }

    public void updateStatus(String message) {
        jda.getPresence().setActivity(Activity.watching(message));
    }

    private void registerCommands() {

        List<SlashCommandData> commands =
                Arrays.stream(CommandType.values())
                        .map(type ->
                                Commands.slash(
                                        type.getName(),
                                        type.getDescription()
                                )
                        )
                        .toList();

        jda.updateCommands()
                .addCommands(commands)
                .queue();
    }
}
