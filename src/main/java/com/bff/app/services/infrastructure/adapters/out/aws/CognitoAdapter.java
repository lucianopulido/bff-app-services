package com.bff.app.services.infrastructure.adapters.out.aws;

import com.bff.app.services.application.dto.LoginResponseDto;
import com.bff.app.services.application.dto.UserResponse;
import com.bff.app.services.domain.model.User;
import com.bff.app.services.domain.port.out.CloudClientPort;
import com.bff.app.services.domain.port.out.CognitoPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
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
    public Mono<UserResponse> createUser(User user) {
        SignUpRequest request = SignUpRequest.builder()
                .clientId(clientId)
                .username(user.getEmail())
                .password(user.getPassword())
                .userAttributes(
                        AttributeType.builder().name("email").value(user.getEmail()).build()
                )
                .build();

        return cognitoClient.createUser(request, SignUpResponse.class)
                .map(response -> new UserResponse(response.userSub(), user.getEmail()));
    }

    @Override
    public Mono<LoginResponseDto> loginUser(User user) {
        InitiateAuthRequest request = InitiateAuthRequest.builder()
                .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .clientId(clientId)
                .authParameters(Map.of(
                        "USERNAME", user.getEmail(),
                        "PASSWORD", user.getPassword()
                ))
                .build();

        return cognitoClient.loginUser(request, InitiateAuthResponse.class)
                .map(InitiateAuthResponse::authenticationResult)
                .map(authResult -> new LoginResponseDto(
                        authResult.idToken(),
                        authResult.accessToken(),
                        authResult.refreshToken()
                ));
    }


}
