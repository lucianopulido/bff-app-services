package com.bff.app.services.domain.port.out;

import reactor.core.publisher.Mono;

public interface CloudClientPort {
    <T, R> Mono<R> createUser(T request, Class<R> responseType);

    <T, R> Mono<R> getUser(T request, Class<R> responseType);

    <T, R> Mono<R> loginUser(T request, Class<R> responseType);

    <T, R> Mono<R> forgotPassword(T request, Class<R> responseType);

    <T, R> Mono<R> confirmForgotPassword(T request, Class<R> responseType);

    <T, R> Mono<R> resendVerificationCode(T request, Class<R> responseType);

    <T, R> Mono<R> confirmEmail(T request, Class<R> responseType);
}
