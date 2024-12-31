package com.bff.app.services.application.usecase;

import com.bff.app.services.application.dto.LoginRequestDto;
import com.bff.app.services.application.dto.LoginResponseDto;
import com.bff.app.services.application.dto.UserResponse;
import com.bff.app.services.domain.model.User;
import com.bff.app.services.domain.port.in.UserUseCase;
import com.bff.app.services.domain.port.out.CognitoPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class UserUseCaseImpl implements UserUseCase {

    private final CognitoPort cognitoPort;

    public UserUseCaseImpl(CognitoPort cognitoPort) {
        this.cognitoPort = cognitoPort;
    }

    @Override
    public Mono<UserResponse> createOrder(LoginRequestDto request) {
        User user = new User(request.getEmail(), request.getPassword());
        return cognitoPort.createUser(user);
    }

    @Override
    public Mono<LoginResponseDto> loginUser(LoginRequestDto request) {
        User user = new User(request.getEmail(), request.getPassword());
        return cognitoPort.loginUser(user);
    }

}
