package dev.edu.ngochandev.userservice.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateRoleRequestDto {
    @NotBlank(message = "error.name.not-blank")
    private String name;
    private String description;
    private List<String> permissionIds;
}
