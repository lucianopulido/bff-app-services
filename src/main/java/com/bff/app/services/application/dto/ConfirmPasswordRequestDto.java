package com.bff.app.services.application.dto;

import lombok.Data;

@Data
public class ConfirmPasswordRequestDto {
    private String email;
    private String confirmationCode;
    private String newPassword;
}
