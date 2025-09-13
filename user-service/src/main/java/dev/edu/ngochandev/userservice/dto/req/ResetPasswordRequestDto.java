package dev.edu.ngochandev.userservice.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequestDto extends TokenRequestDto {
    private String newPassword;

}
