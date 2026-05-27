package com.example.mc_monitor.module;

import com.example.mc_monitor.entity.Audit;
import com.example.mc_monitor.entity.User;
import com.example.mc_monitor.model.ServerJoinEvent;
import com.example.mc_monitor.repository.AuditRepository;
import com.example.mc_monitor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServerJoinEventHandler implements EventHandler<ServerJoinEvent, Void> {

    private final String MODULE_NAME = "[ServerJoinEventHandler] ";

    private final UserRepository userRepository;
    private final AuditRepository auditRepository;

    @Override
    public boolean support(Object event) {
        return event instanceof ServerJoinEvent;
    }

    @Override
    public Mono<Void> handle(ServerJoinEvent event) {
        return Mono.just(event)
            .flatMap(this::validateServerJoinEvent)
            .doOnNext(e -> log.info(MODULE_NAME + "Successfully validated event : {}", e))
            .map(this::toAudit)
            .flatMap(auditRepository::save)
            .doOnSuccess(savedAudit -> log.info(MODULE_NAME + "Successfully saved audit log : {}",
                savedAudit))
            .doOnError(
                e -> log.error(MODULE_NAME + "Failed to save audit log for {}: {}", event.getName(),
                    e.getMessage()))
            .then();
    }

    private Mono<ServerJoinEvent> validateServerJoinEvent(ServerJoinEvent event) {
        if (event.getUuid() == null || event.getUuid().isBlank()) {
            return Mono.error(
                new IllegalArgumentException(MODULE_NAME + "UUID is null : " + event));
        }

        return userRepository.findByUuid(event.getUuid())
            .doOnNext(user -> log.info(MODULE_NAME + "Existing user joined : {}", user))
            .switchIfEmpty(Mono.defer(() -> {
                log.info(MODULE_NAME + "New user detected, creating : {}", event.getName());
                return userRepository.save(toUser(event))
                    .doOnSuccess(
                        user -> log.info(MODULE_NAME + "Successfully created user : {}", user))
                    .doOnError(
                        e -> log.error(MODULE_NAME + "Failed to create user : {} - Reason : {}",
                            event.getName(), e.getMessage()));
            }))
            .thenReturn(event);
    }

    private User toUser(ServerJoinEvent event) {
        User newUser = User.builder()
            .uuid(event.getUuid())
            .userId(event.getName())
            .createTime(UtilManager.now())
            .build();

        return newUser;
    }

    private Audit toAudit(ServerJoinEvent serverJoinEvent) {
        return Audit.builder()
            .uuid(serverJoinEvent.getUuid())
            .name(serverJoinEvent.getName())
            .type(serverJoinEvent.getType())
            .createdAt(UtilManager.convertStringToLocalDateTime(serverJoinEvent.getTime()))
            .build();
    }
}
