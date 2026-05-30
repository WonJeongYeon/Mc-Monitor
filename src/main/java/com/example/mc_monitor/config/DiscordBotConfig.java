package com.example.mc_monitor.config;

import com.example.mc_monitor.discord.command.Command;
import com.example.mc_monitor.discord.command.CommandManager;
import com.example.mc_monitor.discord.command.impl.ServerCommand;
import com.example.mc_monitor.discord.listener.SlashCommandListener;
import com.example.mc_monitor.model.McServerStatus;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DiscordBotConfig {

    @Value("${discord.token}")
    private String token;

    @Bean
    public CommandManager commandManager( //명령어 목록 초기화
            List<Command> commands
    ) {
        log.info("[DiscordBotConfig] Initializing CommandManager...");
        CommandManager manager =
                new CommandManager();

        commands.forEach(manager::register);

        return manager;
    }

    @Bean
    public SlashCommandListener slashCommandListener( //명령어 리스너 초기화
            CommandManager commandManager
    ) {
        log.info("[DiscordBotConfig] Initializing SlashCommandManager...");
        return new SlashCommandListener(
                commandManager
        );
    }

    @Bean
    public JDA jda( //BOT 최종 초기화
            SlashCommandListener listener
    ) throws Exception {
        log.info("[DiscordBotConfig] Initializing JDA...");
        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(listener)
                .build()
                .awaitReady();

        List<SlashCommandData> commands =
                List.of(
                        Commands.slash(
                                "server",
                                "서버 상태 확인"
                        )
                );

        jda.updateCommands()
                .addCommands(commands)
                .queue();

        return jda;
    }
}