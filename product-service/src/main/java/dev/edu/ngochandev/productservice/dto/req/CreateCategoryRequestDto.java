package dev.edu.ngochandev.productservice.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequestDto {
    @NotBlank(message = "Name must not be blank")
    private String name;
    private String description;
    @NotBlank(message = "Slug must not be blank")
    private String slug;
    private String thumbnail;
    private String parentId;
}
