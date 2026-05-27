package com.example.mc_monitor.discord.command.impl;

import com.example.mc_monitor.config.McServerState;
import com.example.mc_monitor.discord.command.Command;
import com.example.mc_monitor.discord.command.CommandType;
import com.example.mc_monitor.model.McServerStatus;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServerCommand implements Command {

    private final McServerState state;

    @Value("${mc.server.ip}")
    private String serverAddress;

    @Override
    public CommandType getType() {
        return CommandType.SERVER;
    }

    @Override
    public void execute(
            SlashCommandInteractionEvent event
    ) {

        McServerStatus status = state.status();

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("서버 정보");

        eb.addField(
                "서버 IP",
                serverAddress,
                true
        );

        eb.addField(
                "플레이어",
                status.getPlayers()
                        + "/"
                        + status.getMaxPlayers(),
                true
        );

        eb.addField(
                "TPS",
                String.valueOf(status.getTps()),
                true
        );

        event.replyEmbeds(eb.build())
                .queue();
    }
}
