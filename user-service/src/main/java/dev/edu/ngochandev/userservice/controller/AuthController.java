package dev.edu.ngochandev.userservice.controller;

import dev.edu.ngochandev.sharedkernel.common.Translator;
import dev.edu.ngochandev.sharedkernel.dto.res.SuccessResponseDto;
import dev.edu.ngochandev.userservice.dto.req.RegisterUserRequestDto;
import dev.edu.ngochandev.userservice.dto.res.TokenResponseDto;
import dev.edu.ngochandev.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final Translator translator;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<TokenResponseDto> register(@RequestBody @Valid RegisterUserRequestDto registerUserRequestDto) {
        return SuccessResponseDto.<TokenResponseDto>builder()
                .message(translator.translate("user.register.success"))
                .httpStatus(HttpStatus.CREATED)
                .data(authService.register(registerUserRequestDto))
                .build();
    }
}
