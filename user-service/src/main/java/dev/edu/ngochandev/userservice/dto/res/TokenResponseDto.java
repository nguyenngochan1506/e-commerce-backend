package dev.edu.ngochandev.userservice.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}
