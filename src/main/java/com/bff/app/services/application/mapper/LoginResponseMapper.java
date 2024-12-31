package com.bff.app.services.application.mapper;

import com.bff.app.services.application.dto.LoginResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;

@Mapper
public interface LoginResponseMapper {

    LoginResponseMapper INSTANCE = Mappers.getMapper(LoginResponseMapper.class);
    @Mappings({
        @Mapping(target = "idToken", expression = "java(authenticationResultType.idToken())"),
        @Mapping(target = "accessToken", expression = "java(authenticationResultType.accessToken())"),
        @Mapping(target = "refreshToken", expression = "java(authenticationResultType.refreshToken())")
    })
    LoginResponseDto toLoginResponseDto(AuthenticationResultType authenticationResultType);
}
