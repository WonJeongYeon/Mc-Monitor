package com.example.mc_monitor.module;

import reactor.core.publisher.Mono;

public interface EventHandler<E, T> {

    boolean support(Object event);

    Mono<T> handle(E event);
}
