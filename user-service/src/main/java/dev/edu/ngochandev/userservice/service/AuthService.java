package dev.edu.ngochandev.userservice.service;

import dev.edu.ngochandev.userservice.dto.req.LoginRequestDto;
import dev.edu.ngochandev.userservice.dto.req.RegisterUserRequestDto;
import dev.edu.ngochandev.userservice.dto.res.TokenResponseDto;

public interface AuthService {
    TokenResponseDto register(RegisterUserRequestDto req);

    TokenResponseDto authenticate(LoginRequestDto req);
}
