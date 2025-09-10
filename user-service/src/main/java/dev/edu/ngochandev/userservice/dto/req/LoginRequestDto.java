package dev.edu.ngochandev.userservice.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @NotBlank(message = "error.validation.not-blank")
    private String identifier; // username or email
    @NotBlank(message = "error.validation.not-blank")
    private String password;
}
