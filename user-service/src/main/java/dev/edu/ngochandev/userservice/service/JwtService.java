package dev.edu.ngochandev.userservice.service;


import dev.edu.ngochandev.userservice.common.TokenType;
import dev.edu.ngochandev.userservice.entity.UserEntity;

import java.util.Date;

public interface JwtService {
    String generateToken(UserEntity user, TokenType type);

    boolean validateToken(String token, TokenType type) ;

    String extractUsername(String token) ;

    String extractJti(String token) ;

    Date extractExpiration(String token) ;

//    String disableToken(InvalidatedTokenEntity invalidatedToken);
}
