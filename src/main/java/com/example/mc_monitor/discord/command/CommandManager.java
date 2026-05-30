package com.example.mc_monitor.discord.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private final Map<CommandType, Command> commands =
            new HashMap<>();

    public void register(Command command) {
        commands.put(
                command.getType(),
                command
        );
    }

    public void handle(SlashCommandInteractionEvent event) {
        CommandType type =
                CommandType.from(event.getName());
        if (type == null) {
            event.reply("알 수 없는 명령어")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        Command command = commands.get(type);
        if (command == null) {
            event.reply("구현되지 않은 명령어")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        command.execute(event);
    }
}
