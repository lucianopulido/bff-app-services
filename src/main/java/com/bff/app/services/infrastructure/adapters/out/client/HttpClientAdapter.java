package com.bff.app.services.infrastructure.adapters.out.client;

import com.bff.app.services.domain.port.out.HttpClientPort;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class HttpClientAdapter implements HttpClientPort {

    private final WebClient webClient;

    public HttpClientAdapter(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public <T, K> Mono<K> post(String url, T body, MultiValueMap<String, String> headers) {
        return webClient.post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }


    @Override
    public <K> Mono<K> get(String url, MultiValueMap<String, String> headers) {
        return webClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public <T, K> Mono<K> put(String url, T body, MultiValueMap<String, String> headers) {
        return webClient.put()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public <K> Mono<K> delete(String url, MultiValueMap<String, String> headers) {
        return webClient.delete()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }
}
