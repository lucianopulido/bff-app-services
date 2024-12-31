package com.bff.app.services.domain.port.out;

import reactor.core.publisher.Mono;

public interface CloudClientPort {
    <T, R> Mono<R> createUser(T request, Class<R> responseType);

    <T, R> Mono<R> getUser(T request, Class<R> responseType);

    <T, R> Mono<R> loginUser(T request, Class<R> responseType);
}
