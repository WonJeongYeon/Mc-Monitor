package com.example.mc_monitor.module;

import jakarta.annotation.PostConstruct;

public class PlayerManager {

    @PostConstruct
    public void init() {
        run();
    }

    private void run() {
        // 여기다 일반코드건 Webflux코드건 자유로이 작성
    }

}
