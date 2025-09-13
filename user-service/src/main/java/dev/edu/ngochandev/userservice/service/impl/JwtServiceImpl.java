package dev.edu.ngochandev.userservice.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dev.edu.ngochandev.sharedkernel.exception.UnauthorizedException;
import dev.edu.ngochandev.userservice.common.TokenType;
import dev.edu.ngochandev.userservice.entity.InvalidatedTokenEntity;
import dev.edu.ngochandev.userservice.entity.RoleEntity;
import dev.edu.ngochandev.userservice.entity.UserEntity;
import dev.edu.ngochandev.userservice.repository.InvalidatedTokenRepository;
import dev.edu.ngochandev.userservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j(topic = "JwtService")
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final InvalidatedTokenRepository invalidatedTokenRepository;
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
                    .jwtID(UUID.randomUUID().toString())
                    .claim("username", user.getUsername())
                    .claim("email", user.getEmail())
                    .claim("roles", user.getRoles().stream().map(RoleEntity::getName).toList())
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
        return switch (type){
            case ACCESS -> validateToken(token, accessTokenSecretKey);
            case REFRESH -> validateToken(token, refreshTokenSecretKey);
            case EMAIL_VERIFICATION -> validateToken(token, verifyEmailTokenSecretKey);
            case PASSWORD_RESET -> validateToken(token, resetPasswordTokenSecretKey);
        };
    }
    private boolean validateToken(String token, String secretKey) {
        try{
            JWSVerifier verifier = new MACVerifier(secretKey);
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            return signedJWT.verify(verifier) && expiration.after(new Date()) && !invalidatedTokenRepository.existsByJti(signedJWT.getJWTClaimsSet().getJWTID());
        }catch (JOSEException | ParseException e) {
            throw new UnauthorizedException("error.token.invalid");
        }
    }

    @Override
    public String extractUsername(String token) {
        try {
            return SignedJWT.parse(token).getJWTClaimsSet().getStringClaim("username");
        } catch (ParseException e) {
            throw new UnauthorizedException("error.token.invalid");
        }
    }

    @Override
    public String extractJti(String token) {
        try {
            return SignedJWT.parse(token).getJWTClaimsSet().getJWTID();
        } catch (ParseException e) {
            throw new UnauthorizedException("error.token.invalid");
        }
    }

    @Override
    public Date extractExpiration(String token) {
        try {
            return SignedJWT.parse(token).getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            throw new UnauthorizedException("error.token.invalid");
        }
    }

    @Override
    public String disableToken(String token) {
        String jti = extractJti(token);
        Date expiration = extractExpiration(token);

        InvalidatedTokenEntity invalidatedToken = new InvalidatedTokenEntity();
        invalidatedToken.setJti(jti);
        invalidatedToken.setExpiration(expiration);
        invalidatedTokenRepository.save(invalidatedToken);

        return invalidatedToken.getJti();
    }
}
