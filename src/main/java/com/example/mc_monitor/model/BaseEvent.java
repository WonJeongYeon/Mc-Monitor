package com.example.mc_monitor.model;

import com.example.mc_monitor.model.enums.EventType;
import com.example.mc_monitor.module.UtilManager;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Data
@AllArgsConstructor
public class BaseEvent {

    private EventType type;
    private String uuid;
    private String createdAt;
    private Map<String, Object> payload;

    public static BaseEvent of(EventType type, String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> payload = objectMapper.readValue(body, new TypeReference<>() {
        });

        return new BaseEvent(type, UUID.randomUUID().toString(), UtilManager.now(), payload);
    }

    public Object resolveValueFromPayload(Class<?> type, String key) {
        return type.cast(payload.getOrDefault(key, null));
    }

    public String getEventName() {
        return type.getName();
    }
}
