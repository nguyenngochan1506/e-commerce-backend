package dev.edu.ngochandev.userservice.service.impl;

import dev.edu.ngochandev.sharedkernel.exception.DuplicateResourceException;
import dev.edu.ngochandev.sharedkernel.exception.ResourceNotFoundException;
import dev.edu.ngochandev.sharedkernel.exception.UnauthorizedException;
import dev.edu.ngochandev.userservice.common.TokenType;
import dev.edu.ngochandev.userservice.common.UserStatus;
import dev.edu.ngochandev.userservice.dto.req.LoginRequestDto;
import dev.edu.ngochandev.userservice.dto.req.RegisterUserRequestDto;
import dev.edu.ngochandev.userservice.dto.req.TokenRequestDto;
import dev.edu.ngochandev.userservice.dto.res.TokenResponseDto;
import dev.edu.ngochandev.userservice.entity.UserEntity;
import dev.edu.ngochandev.userservice.repository.UserRepository;
import dev.edu.ngochandev.userservice.service.AuthService;
import dev.edu.ngochandev.userservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    public TokenResponseDto register(RegisterUserRequestDto req) {
        if(userRepository.existsByUsername(req.getUsername())) throw new DuplicateResourceException("error.username.exists");
        if(userRepository.existsByEmail(req.getEmail())) throw new DuplicateResourceException("error.email.exists");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(req.getUsername());
        userEntity.setEmail(req.getEmail());
        userEntity.setPhoneNumber(req.getPhoneNumber());
        userEntity.setPassword(passwordEncoder.encode(req.getPassword()));
        userEntity.setGender(req.getGender());
        userEntity.setDateOfBirth(req.getDateOfBirth());
        userEntity.setAvatar(req.getAvatar());
        userEntity.setFullName(req.getFullName());
        userRepository.save(userEntity);

//        TODO: send verification email
        String verificationToken = jwtService.generateToken(userEntity, TokenType.EMAIL_VERIFICATION);

        return TokenResponseDto.builder()
                .accessToken(verificationToken)
                .refreshToken("dummy-refresh-token")
                .expirationTime(null)
                .build();
    }

    @Override
    public TokenResponseDto authenticate(LoginRequestDto req) {
        UserEntity user = userRepository.findByIdentifier(req.getIdentifier())
                .orElseThrow(() -> new UnauthorizedException("error.invalid.credentials"));

        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())) throw new UnauthorizedException("error.invalid.credentials");
        if(user.getStatus() == UserStatus.INACTIVE) throw new UnauthorizedException("error.user.inactive");
        if(user.getStatus() == UserStatus.BLOCKED) throw new UnauthorizedException("error.user.blocked");

        String accessToken = jwtService.generateToken(user, TokenType.ACCESS);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH);
        Date accessTokenExpiresIn = jwtService.extractExpiration(accessToken);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expirationTime(accessTokenExpiresIn)
                .build();
    }

    @Override
    public String verifyEmail(TokenRequestDto req) {
        String token = req.getToken();
        if(!jwtService.validateToken(token, TokenType.EMAIL_VERIFICATION)) throw new UnauthorizedException("error.token.invalid");

        String username = jwtService.extractUsername(token);
        UserEntity user = userRepository.findByIdentifier(username)
                .orElseThrow(() -> new ResourceNotFoundException("error.user.not-found"));

        if(user.getStatus() == UserStatus.ACTIVE) throw new UnauthorizedException("error.user.already-verified");
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        jwtService.disableToken(token);
        return user.getId();
    }

    @Override
    public TokenResponseDto refreshToken(UserEntity user, String refreshToken) {
        //create new access token and refresh token
        String newAccessToken = jwtService.generateToken(user, TokenType.ACCESS);
        String newRefreshToken = jwtService.generateToken(user, TokenType.REFRESH);
        Date accessTokenExpiresIn = jwtService.extractExpiration(newAccessToken);

        //disable old refresh token
        jwtService.disableToken(refreshToken);

        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expirationTime(accessTokenExpiresIn)
                .build();
    }

    @Override
    public String logout(String token) {
        jwtService.disableToken(token);
        return jwtService.extractJti(token);
    }
}
