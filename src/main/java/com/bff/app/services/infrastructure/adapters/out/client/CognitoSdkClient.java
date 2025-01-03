package com.bff.app.services.infrastructure.adapters.out.client;

import com.bff.app.services.domain.port.out.CloudClientPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ResendConfirmationCodeRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;

@Component
public class CognitoSdkClient implements CloudClientPort {

    private final CognitoIdentityProviderClient cognitoClient;

    public CognitoSdkClient(CognitoIdentityProviderClient cognitoClient) {
        this.cognitoClient = cognitoClient;
    }

    @Override
    public <T, R> Mono<R> createUser(T request, Class<R> responseType) {
        return Mono.fromCallable(() -> responseType.cast(cognitoClient.signUp((SignUpRequest) request)));
    }

    @Override
    public <T, R> Mono<R> getUser(T request, Class<R> responseType) {
        return Mono.fromCallable(() -> responseType.cast(cognitoClient.getUser((GetUserRequest) request)));
    }

    @Override
    public <T, R> Mono<R> loginUser(T request, Class<R> responseType) {
        return Mono.fromCallable(() -> responseType.cast(cognitoClient.initiateAuth((InitiateAuthRequest) request)));
    }

    @Override
    public <T, R> Mono<R> forgotPassword(T request, Class<R> responseType) {
        return Mono.fromCallable(() -> responseType.cast(cognitoClient.forgotPassword((ForgotPasswordRequest) request)));
    }

    @Override
    public <T, R> Mono<R> confirmForgotPassword(T request, Class<R> responseType) {
        return Mono.fromCallable(() -> responseType.cast(cognitoClient.confirmForgotPassword((ConfirmForgotPasswordRequest) request)));
    }

    @Override
    public <T, R> Mono<R> resendVerificationCode(T request, Class<R> responseType) {
        return Mono.fromCallable(() -> responseType.cast(cognitoClient.resendConfirmationCode((ResendConfirmationCodeRequest) request)));
    }

    @Override
    public <T, R> Mono<R> confirmEmail(T request, Class<R> responseType) {
        return Mono.fromCallable(() -> responseType.cast(cognitoClient.confirmSignUp((ConfirmSignUpRequest) request)));
    }
}
