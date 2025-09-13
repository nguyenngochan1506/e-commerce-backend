package dev.edu.ngochandev.userservice.controller;

import dev.edu.ngochandev.sharedkernel.common.Translator;
import dev.edu.ngochandev.sharedkernel.dto.res.SuccessResponseDto;
import dev.edu.ngochandev.userservice.dto.req.LoginRequestDto;
import dev.edu.ngochandev.userservice.dto.req.RegisterUserRequestDto;
import dev.edu.ngochandev.userservice.dto.req.TokenRequestDto;
import dev.edu.ngochandev.userservice.dto.res.TokenResponseDto;
import dev.edu.ngochandev.userservice.entity.UserEntity;
import dev.edu.ngochandev.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto req) {
        return SuccessResponseDto.<TokenResponseDto>builder()
                .message(translator.translate("user.login.success"))
                .httpStatus(HttpStatus.OK)
                .data(authService.authenticate(req))
                .build();
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<TokenResponseDto> refreshToken(@AuthenticationPrincipal UserEntity user, @RequestBody @Valid TokenRequestDto req) {
        return SuccessResponseDto.<TokenResponseDto>builder()
                .message(translator.translate("user.token.refresh.success"))
                .httpStatus(HttpStatus.OK)
                .data(authService.refreshToken(user, req.getToken()))
                .build();
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return SuccessResponseDto.<String>builder()
                .message(translator.translate("user.logout.success"))
                .httpStatus(HttpStatus.OK)
                .data(authService.logout(token))
                .build();
    }

    @PostMapping("/verify-email")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<String> verifyEmail(@RequestBody @Valid TokenRequestDto req) {
        return SuccessResponseDto.<String>builder()
                .message(translator.translate("user.verify-email.success"))
                .httpStatus(HttpStatus.OK)
                .data(authService.verifyEmail(req))
                .build();
    }
}
