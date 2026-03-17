package com.example.mc_monitor.model;

import lombok.Data;

@Data
public class McServerStatus {

    private boolean online;
    private int players;
    private int maxPlayers;
    private double tps;

}
