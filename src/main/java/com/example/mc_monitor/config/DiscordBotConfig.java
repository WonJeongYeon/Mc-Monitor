package com.example.mc_monitor.config;

import com.example.mc_monitor.discord.command.Command;
import com.example.mc_monitor.discord.command.CommandManager;
import com.example.mc_monitor.discord.command.impl.ServerCommand;
import com.example.mc_monitor.discord.listener.SlashCommandListener;
import com.example.mc_monitor.model.McServerStatus;

import lombok.RequiredArgsConstructor;

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
public class DiscordBotConfig {

    @Value("${discord.token}")
    private String token;

    private final McServerState state;

    @Bean
    public CommandManager commandManager(
            List<Command> commands
    ) {

        CommandManager manager =
                new CommandManager();

        commands.forEach(manager::register);

        return manager;
    }
    @Bean
    public SlashCommandListener slashCommandListener(
            CommandManager commandManager
    ) {

        return new SlashCommandListener(
                commandManager
        );
    }

    @Bean
    public JDA jda(
            SlashCommandListener listener
    ) throws Exception {

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