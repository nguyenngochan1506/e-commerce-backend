package dev.edu.ngochandev.userservice.service;

import dev.edu.ngochandev.userservice.dto.req.*;
import dev.edu.ngochandev.userservice.dto.res.TokenResponseDto;
import dev.edu.ngochandev.userservice.entity.UserEntity;
import jakarta.validation.Valid;

public interface AuthService {
    TokenResponseDto register(RegisterUserRequestDto req);

    TokenResponseDto authenticate(LoginRequestDto req);

    String verifyEmail(TokenRequestDto token);

    TokenResponseDto refreshToken(UserEntity user, String refreshToken);

    String logout(String token);

    String changePassword(UserEntity user, ChangePasswordRequestDto req);

    String forgotPassword(ForgotPasswordRequestDto req);

    String resetPassword( ResetPasswordRequestDto req);
}
