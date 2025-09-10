package dev.edu.ngochandev.userservice.service.impl;

import dev.edu.ngochandev.sharedkernel.exception.DuplicateResourceException;
import dev.edu.ngochandev.userservice.dto.req.RegisterUserRequestDto;
import dev.edu.ngochandev.userservice.dto.res.TokenResponseDto;
import dev.edu.ngochandev.userservice.entity.UserEntity;
import dev.edu.ngochandev.userservice.repository.UserRepository;
import dev.edu.ngochandev.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    @Override
    public TokenResponseDto register(RegisterUserRequestDto req) {
        if(userRepository.existsByUsername(req.getUsername())) throw new DuplicateResourceException("error.username.exists");
        if(userRepository.existsByEmail(req.getEmail())) throw new DuplicateResourceException("error.email.exists");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(req.getUsername());
        userEntity.setEmail(req.getEmail());
        userEntity.setPhoneNumber(req.getPhoneNumber());
        userEntity.setPassword(req.getPassword());  // TODO: hash password
        userEntity.setGender(req.getGender());
        userEntity.setDateOfBirth(req.getDateOfBirth());
        userEntity.setAvatar(req.getAvatar());
        userEntity.setFullName(req.getFullName());
        userRepository.save(userEntity);

//        TODO: send verification email

//        TODO: generate JWT token

        return TokenResponseDto.builder()
                .accessToken("dummy-access-token")
                .refreshToken("dummy-refresh-token")
                .accessTokenExpiresIn(123455678L)
                .build();
    }


}
