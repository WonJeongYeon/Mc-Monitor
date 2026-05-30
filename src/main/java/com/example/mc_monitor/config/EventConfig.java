package com.example.mc_monitor.config;

import com.example.mc_monitor.model.enums.EventType;
import com.example.mc_monitor.module.handler.EventHandler;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Bean
    public Map<EventType, List<EventHandler>> getEventHandlerMap(List<EventHandler> eventHandlers) {
        return eventHandlers.stream()
            .collect(Collectors.groupingBy(EventHandler::getEventType));
    }
}
