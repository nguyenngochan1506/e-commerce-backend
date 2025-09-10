package dev.edu.ngochandev.userservice.controller;

import dev.edu.ngochandev.sharedkernel.dto.res.SuccessResponseDto;
import dev.edu.ngochandev.userservice.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<Object> getMe(@AuthenticationPrincipal UserEntity user) {
        return SuccessResponseDto.builder()
                .httpStatus(HttpStatus.OK)
                .message("Get current user successfully")
                .data(user)
                .build();
    }
}
