package dev.edu.ngochandev.userservice.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.edu.ngochandev.userservice.common.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterUserRequestDto {
    @NotBlank(message = "error.username.not-blank")
    private String username;
    @NotBlank(message = "error.email.not-blank")
    private String email;
    @NotBlank(message = "error.phone-number.not-blank")
    private String phoneNumber;
    @NotBlank(message = "error.password.not-blank")
    private String password;
    @NotBlank(message = "error.full-name.not-blank")
    private String fullName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;
    private String avatar;
    private Gender gender;
}
