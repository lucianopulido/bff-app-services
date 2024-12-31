package com.bff.app.services.application.usecase;

import com.bff.app.services.application.dto.LoginRequestDto;
import com.bff.app.services.application.dto.LoginResponseDto;
import com.bff.app.services.application.dto.UserResponse;
import com.bff.app.services.application.mapper.LoginResponseMapper;
import com.bff.app.services.application.mapper.UserResponseMapper;
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
        return cognitoPort.createUser(user).map(signUpResponse -> {
            UserResponse response = UserResponseMapper.INSTANCE.toUserResponse(signUpResponse);
            response.setEmail(request.getEmail());
            return response;
        });
    }


    @Override
    public Mono<LoginResponseDto> loginUser(LoginRequestDto request) {
        User user = new User(request.getEmail(), request.getPassword());
        return cognitoPort.loginUser(user).map(LoginResponseMapper.INSTANCE::toLoginResponseDto);
    }

    @Override
    public Mono<Void> initiateForgotPassword(String email) {
        return cognitoPort.initiateForgotPassword(email);
    }

    @Override
    public Mono<Void> confirmForgotPassword(String email, String confirmationCode, String newPassword) {
        return cognitoPort.confirmForgotPassword(email, confirmationCode, newPassword);
    }

}
