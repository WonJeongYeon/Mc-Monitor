package com.example.mc_monitor.module.handler;

import com.example.mc_monitor.model.BaseEvent;
import com.example.mc_monitor.model.enums.EventType;
import reactor.core.publisher.Mono;

public interface EventHandler {

    EventType getEventType();

    Mono<Void> handle(BaseEvent event);
}
