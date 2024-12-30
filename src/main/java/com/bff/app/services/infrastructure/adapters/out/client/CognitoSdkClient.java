package com.bff.app.services.infrastructure.adapters.out.client;

import com.bff.app.services.domain.port.out.CloudClientPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserRequest;

@Component
public class CognitoSdkClient implements CloudClientPort {

    private final CognitoIdentityProviderClient cognitoClient;

    public CognitoSdkClient(CognitoIdentityProviderClient cognitoClient) {
        this.cognitoClient = cognitoClient;
    }

    @Override
    public <T, R> Mono<R> createUser(T request, Class<R> responseType) {
        return Mono.fromCallable(() -> responseType.cast(cognitoClient.adminCreateUser((AdminCreateUserRequest) request)))
                .onErrorMap(ex -> new RuntimeException("Error creating user: " + ex.getMessage()));
    }

    @Override
    public <T, R> Mono<R> getUser(T request, Class<R> responseType) {
        return Mono.fromCallable(() -> responseType.cast(cognitoClient.adminGetUser((AdminGetUserRequest) request)))
                .onErrorMap(ex -> new RuntimeException("Error retrieving user: " + ex.getMessage()));
    }
}
