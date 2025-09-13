package dev.edu.ngochandev.userservice.service;

import dev.edu.ngochandev.userservice.dto.req.ChangePasswordRequestDto;
import dev.edu.ngochandev.userservice.dto.req.LoginRequestDto;
import dev.edu.ngochandev.userservice.dto.req.RegisterUserRequestDto;
import dev.edu.ngochandev.userservice.dto.req.TokenRequestDto;
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
}
