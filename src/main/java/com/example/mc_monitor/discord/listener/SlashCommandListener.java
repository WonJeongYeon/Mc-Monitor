package com.example.mc_monitor.discord.listener;

import com.example.mc_monitor.discord.command.CommandManager;

import lombok.RequiredArgsConstructor;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@RequiredArgsConstructor
public class SlashCommandListener
        extends ListenerAdapter {

    private final CommandManager commandManager;

    @Override
    public void onSlashCommandInteraction(
            SlashCommandInteractionEvent event
    ) {

        commandManager.handle(event);
    }
}
