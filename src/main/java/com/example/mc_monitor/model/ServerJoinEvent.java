package com.example.mc_monitor.model;

import lombok.Data;

@Data
public class ServerJoinEvent {
    private String type;
    private String name;
    private String uuid;
    private String time;
}
