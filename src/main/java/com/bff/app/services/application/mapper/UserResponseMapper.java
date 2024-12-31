package com.bff.app.services.application.mapper;

import com.bff.app.services.application.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;

@Mapper
public interface UserResponseMapper {

    UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);

    @Mappings({
            @Mapping(target = "username", expression = "java(signUpResponse.userSub())"),
            @Mapping(target = "email", ignore = true)
    })
    public UserResponse toUserResponse(SignUpResponse signUpResponse);
}