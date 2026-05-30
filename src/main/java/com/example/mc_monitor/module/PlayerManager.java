package com.example.mc_monitor.module;

import com.example.mc_monitor.model.BaseEvent;
import com.example.mc_monitor.model.enums.EventType;
import com.example.mc_monitor.module.handler.EventHandler;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerManager {

    private final String MODULE_NAME = "[PlayerManager] ";
    private final Map<EventType, List<EventHandler>> eventHandlers;

    public Mono<Void> processEvent(BaseEvent event) {
        if (event == null) {
            return null;
        }

        return Flux.fromIterable(eventHandlers.get(event.getType()))
            .flatMap(handler -> handler.handle(event)
                .onErrorResume(e -> {
                    log.error(MODULE_NAME + "Failed to execute handler {}: {}",
                        handler.getClass().getSimpleName(), e.getMessage());
                    return Mono.empty();
                }))
            .then();
    }
}
