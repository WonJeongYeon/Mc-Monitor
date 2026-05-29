package com.example.mc_monitor.module;

import java.util.List;
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
    private final List<EventHandler<?, ?>> eventHandlers;

    public Mono<?> processEvent(Object event) {
        return Flux.fromIterable(eventHandlers)
            .filter(handler -> handler.support(event))
            .flatMap(handler -> {
                EventHandler<Object, ?> targetHandler = (EventHandler<Object, ?>) handler;
                return targetHandler.handle(event)
                    .onErrorResume(e -> {
                        log.error(MODULE_NAME + "Failed to execute handler {}: {}",
                            handler.getClass().getSimpleName(), e.getMessage());
                        return Mono.empty();
                    });
            })
            .then();
    }
}
