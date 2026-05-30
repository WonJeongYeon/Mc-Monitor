package com.example.mc_monitor.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {

    PLAYER_JOIN_EVENT("Player join event", "플레이어 입장 이벤트");

    private final String name;
    private final String description;
}
