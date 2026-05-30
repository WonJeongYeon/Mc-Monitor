package com.example.mc_monitor.module.handler;

import com.example.mc_monitor.entity.Audit;
import com.example.mc_monitor.entity.User;
import com.example.mc_monitor.model.BaseEvent;
import com.example.mc_monitor.model.enums.EventType;
import com.example.mc_monitor.module.UtilManager;
import com.example.mc_monitor.repository.AuditRepository;
import com.example.mc_monitor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServerJoinEventHandler implements EventHandler {

    private final String MODULE_NAME = "[ServerJoinEventHandler] ";

    private final UserRepository userRepository;
    private final AuditRepository auditRepository;

    @Override
    public EventType getEventType() {
        return EventType.PLAYER_JOIN_EVENT;
    }

    @Override
    public Mono<Void> handle(BaseEvent event) {
        return Mono.just(event)
            .flatMap(this::validateServerJoinEvent)
            .doOnNext(e -> log.info(MODULE_NAME + "Successfully validated event : {}", e))
            .map(this::toAudit)
            .flatMap(auditRepository::save)
            .doOnSuccess(savedAudit -> log.info(MODULE_NAME + "Successfully saved audit log : {}",
                savedAudit))
            .doOnError(
                e -> log.error(MODULE_NAME + "Failed to save audit log for {}: {}",
                    event.getEventName(),
                    e.getMessage()))
            .then();
    }

    private Mono<BaseEvent> validateServerJoinEvent(BaseEvent event) {
        if (event.getUuid() == null || event.getUuid().isBlank()) {
            return Mono.error(
                new IllegalArgumentException(MODULE_NAME + "UUID is null : " + event));
        }

        return userRepository.findByUuid(event.getUuid())
            .doOnNext(user -> log.info(MODULE_NAME + "Existing user joined : {}", user))
            .switchIfEmpty(Mono.defer(() -> {
                log.info(MODULE_NAME + "New user detected, creating : {}", event.getEventName());
                return userRepository.save(toUser(event))
                    .doOnSuccess(
                        user -> log.info(MODULE_NAME + "Successfully created user : {}", user))
                    .doOnError(
                        e -> log.error(MODULE_NAME + "Failed to create user : {} - Reason : {}",
                            event.getEventName(), e.getMessage()));
            }))
            .thenReturn(event);
    }

    private User toUser(BaseEvent event) {
        return User.builder()
            .uuid((String) event.resolveValueFromPayload(String.class, "uuid"))
            .userId((String) event.resolveValueFromPayload(String.class, "userId"))
            .createTime(UtilManager.now())
            .build();
    }

    private Audit toAudit(BaseEvent event) {
        return Audit.builder()
            .uuid((String) event.resolveValueFromPayload(String.class, "uuid"))
            .name((String) event.resolveValueFromPayload(String.class, "name"))
            .type(event.getEventName())
            .createdAt(UtilManager.convertStringToLocalDateTime(event.getCreatedAt()))
            .build();
    }
}
