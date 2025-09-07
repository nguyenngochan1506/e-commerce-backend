package dev.edu.ngochandev.productservice.dto.req;

import dev.edu.ngochandev.productservice.common.ProductAttributes;
import dev.edu.ngochandev.productservice.common.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Jacksonized
public class CreateProductRequestDto {
    @NotBlank(message = "error.name.not-blank")
    private String name;

    private String description;

    @NotBlank(message = "error.slug.not-blank")
    private String slug;

    private ProductStatus status;

    @NotBlank(message = "error.thumbnail.not-blank")
    private String thumbnail;

    @NotBlank(message = "error.category-id.not-blank")
    private String categoryId;

    @NotNull(message = "error.attributes.not-null")
    private List<ProductAttributes> attributes;
}
