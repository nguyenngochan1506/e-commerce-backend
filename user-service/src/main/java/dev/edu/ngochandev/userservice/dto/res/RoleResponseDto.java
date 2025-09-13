package dev.edu.ngochandev.userservice.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RoleResponseDto {
    private String id;
    private String name;
    private String description;
}
