package dev.edu.ngochandev.userservice.service.impl;

import dev.edu.ngochandev.userservice.dto.req.RegisterUserRequestDto;
import dev.edu.ngochandev.userservice.dto.res.UserResponseDto;
import dev.edu.ngochandev.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Override
    public UserResponseDto register(RegisterUserRequestDto req) {
        return null;
    }
}
