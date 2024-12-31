package com.bff.app.services.infrastructure.adapters.out.aws;

import com.bff.app.services.domain.model.User;
import com.bff.app.services.domain.port.out.CloudClientPort;
import com.bff.app.services.domain.port.out.CognitoPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

import java.util.Map;

@Component
public class CognitoAdapter implements CognitoPort {

    private final CloudClientPort cognitoClient;

    @Value("${aws.user.pool}")
    private String userPoolId;

    @Value("${aws.user.pool.client-id}")
    private String clientId;

    public CognitoAdapter(CloudClientPort cognitoClient) {
        this.cognitoClient = cognitoClient;
    }

    @Override
    public Mono<SignUpResponse> createUser(User user) {
        SignUpRequest request = SignUpRequest.builder()
                .clientId(clientId)
                .username(user.getEmail())
                .password(user.getPassword())
                .userAttributes(
                        AttributeType.builder().name("email").value(user.getEmail()).build()
                )
                .build();

        return cognitoClient.createUser(request, SignUpResponse.class);
    }

    @Override
    public Mono<AuthenticationResultType> loginUser(User user) {
        InitiateAuthRequest request = InitiateAuthRequest.builder()
                .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .clientId(clientId)
                .authParameters(Map.of(
                        "USERNAME", user.getEmail(),
                        "PASSWORD", user.getPassword()
                ))
                .build();

        return cognitoClient.loginUser(request, InitiateAuthResponse.class)
                .map(InitiateAuthResponse::authenticationResult);
    }

    @Override
    public Mono<Void> initiateForgotPassword(String email) {
        ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                .clientId(clientId)
                .username(email)
                .build();

        return cognitoClient.forgotPassword(request, ForgotPasswordResponse.class).then();
    }

    @Override
    public Mono<Void> confirmForgotPassword(String email, String confirmationCode, String newPassword) {
        ConfirmForgotPasswordRequest request = ConfirmForgotPasswordRequest.builder()
                .clientId(clientId)
                .username(email)
                .confirmationCode(confirmationCode)
                .password(newPassword)
                .build();

        return cognitoClient.confirmForgotPassword(request, ConfirmForgotPasswordResponse.class).then();
    }

}
