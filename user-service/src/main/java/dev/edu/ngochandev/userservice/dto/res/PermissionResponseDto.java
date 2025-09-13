package dev.edu.ngochandev.userservice.dto.res;

import dev.edu.ngochandev.userservice.common.HttpMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PermissionResponseDto {
    private String id;
    private String name;
    private String description;
    private String module;
    private HttpMethod method;
    private String path;
}
