package dev.edu.ngochandev.userservice.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dev.edu.ngochandev.sharedkernel.exception.UnauthorizedException;
import dev.edu.ngochandev.userservice.common.TokenType;
import dev.edu.ngochandev.userservice.entity.UserEntity;
import dev.edu.ngochandev.userservice.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@Slf4j(topic = "JwtService")
public class JwtServiceImpl implements JwtService {
    @Value("${app.jwt.access-token.secret}")
    private String accessTokenSecretKey;
    @Value("${app.jwt.access-token.expiration}")
    private Long accessTokenExpiration;
    @Value("${app.jwt.refresh-token.secret}")
    private String refreshTokenSecretKey;
    @Value("${app.jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;
    @Value("${app.jwt.verification-token.secret}")
    private String verifyEmailTokenSecretKey;
    @Value("${app.jwt.verification-token.expiration}")
    private Long verifyEmailTokenExpiration;
    @Value("${app.jwt.reset-password-token.secret}")
    private String resetPasswordTokenSecretKey;
    @Value("${app.jwt.reset-password-token.expiration}")
    private Long resetPasswordTokenExpiration;
    @Value("${app.jwt.issuer}")
    private String issuer;

    @Override
    public String generateToken(UserEntity user, TokenType type) {
        return switch (type){
            case ACCESS -> generateToken(user, accessTokenSecretKey, accessTokenExpiration);
            case REFRESH -> generateToken(user, refreshTokenSecretKey, refreshTokenExpiration);
            case EMAIL_VERIFICATION -> generateToken(user, verifyEmailTokenSecretKey, verifyEmailTokenExpiration);
            case PASSWORD_RESET -> generateToken(user, resetPasswordTokenSecretKey, resetPasswordTokenExpiration);
        };
    }

    private String generateToken(UserEntity user, String secretKey, Long expiration) {
        try {
            //header
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            // claims
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .issueTime(new Date())
                    .expirationTime(new Date(Instant.now().plus(expiration, ChronoUnit.SECONDS).toEpochMilli()))
                    .subject(user.getId())
                    .issuer(issuer)
                    .claim("username", user.getUsername())
                    .claim("email", user.getEmail())
                    .claim("roles", List.of("NORMAL", "ADMIN"))
                    .build();
            // payload
            Payload payload = new Payload(claims.toJSONObject());
            JWSObject jwsObject = new JWSObject(header, payload);
            jwsObject.sign(new MACSigner(secretKey));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error while generating token: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateToken(String token, TokenType type) {
        return false;
    }

    @Override
    public String extractUsername(String token) {
        return "";
    }

    @Override
    public String extractJti(String token) {
        return "";
    }

    @Override
    public Date extractExpiration(String token) {
        try {
            return SignedJWT.parse(token).getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            throw new UnauthorizedException("error.token.invalid");
        }
    }
}
