package com.example.mc_monitor.discord.command;

import lombok.Getter;

@Getter
public enum CommandType {

    SERVER(
            "server",
            "서버 상태 확인"
    ),

    PING(
            "ping",
            "핑 테스트"
    );

    private final String name;

    private final String description;

    CommandType(
            String name,
            String description
    ) {
        this.name = name;
        this.description = description;
    }

    public static CommandType from(String value) {

        for (CommandType type : values()) {
            if (type.name.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return null;
    }
}