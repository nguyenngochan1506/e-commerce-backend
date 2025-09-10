package dev.edu.ngochandev.userservice.service;

import dev.edu.ngochandev.userservice.dto.req.RegisterUserRequestDto;
import dev.edu.ngochandev.userservice.dto.res.UserResponseDto;

public interface AuthService {
    UserResponseDto register(RegisterUserRequestDto req);
}
