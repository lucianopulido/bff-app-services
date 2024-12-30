package com.bff.app.services.infrastructure.adapters.out.aws;

import com.bff.app.services.application.dto.UserResponse;
import com.bff.app.services.domain.model.User;
import com.bff.app.services.domain.port.out.CloudClientPort;
import com.bff.app.services.domain.port.out.CognitoPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

@Component
public class CognitoAdapter implements CognitoPort {

    private final CloudClientPort cognitoClient;

    @Value("${aws.user.pool}")
    private String userPoolId;

    public CognitoAdapter(CloudClientPort cognitoClient) {
        this.cognitoClient = cognitoClient;
    }

    @Override
    public Mono<UserResponse> createUser(User user) {
        AdminCreateUserRequest request = AdminCreateUserRequest.builder()
                .userPoolId(userPoolId)
                .username(user.getUsername())
                .userAttributes(
                        AttributeType.builder().name("email").value(user.getEmail()).build(),
                        AttributeType.builder().name("email_verified").value("true").build(),
                        AttributeType.builder().name("custom:password").value(user.getPassword()).build()
                )
                .temporaryPassword(user.getPassword())
                .build();

        return cognitoClient.createUser(request, AdminCreateUserResponse.class)
                .map(response -> new UserResponse(response.user().username(), extractEmail(response)));
    }


    private String extractEmail(AdminCreateUserResponse response) {
        return response.user().attributes().stream()
                .filter(attr -> "email".equals(attr.name()))
                .findFirst()
                .map(AttributeType::value)
                .orElse(null);
    }
}
