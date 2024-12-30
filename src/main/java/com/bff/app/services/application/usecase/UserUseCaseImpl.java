package com.bff.app.services.application.usecase;

import com.bff.app.services.application.dto.CreateUserRequest;
import com.bff.app.services.application.dto.UserResponse;
import com.bff.app.services.domain.model.User;
import com.bff.app.services.domain.port.in.UserUseCase;
import com.bff.app.services.domain.port.out.CognitoPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class UserUseCaseImpl implements UserUseCase {

    private final CognitoPort cognitoClient;

    public UserUseCaseImpl(CognitoPort cognitoClient) {
        this.cognitoClient = cognitoClient;
    }

    @Override
    public Mono<UserResponse> createOrder(CreateUserRequest request) {
        User user = new User(request.getEmail(), request.getPassword());
        return cognitoClient.createUser(user);
    }

}
