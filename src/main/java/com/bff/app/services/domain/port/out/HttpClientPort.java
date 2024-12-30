package com.bff.app.services.domain.port.out;

import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

public interface HttpClientPort {

    <T, K> Mono<K> post(String url, T body, MultiValueMap<String, String> headers);

    <K> Mono<K> get(String url, MultiValueMap<String, String> headers);

    <T, K> Mono<K> put(String url, T body, MultiValueMap<String, String> headers);

    <K> Mono<K> delete(String url, MultiValueMap<String, String> headers);
}
