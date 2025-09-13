package dev.edu.ngochandev.userservice.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class ChangePasswordRequestDto {
    @NotBlank(message = "error.validation.not-blank")
    private String oldPassword;
    @NotBlank(message = "error.validation.not-blank")
    private String newPassword;
}
