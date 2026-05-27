package com.example.mc_monitor.discord.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface Command {

    CommandType getType();

    void execute(SlashCommandInteractionEvent event);

}
