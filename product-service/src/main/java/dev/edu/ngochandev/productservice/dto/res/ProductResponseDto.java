package dev.edu.ngochandev.productservice.dto.res;

import dev.edu.ngochandev.productservice.common.ProductAttributes;
import dev.edu.ngochandev.productservice.common.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ProductResponseDto {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String thumbnail;
    private ProductStatus status;
    private List<CategoryResponseDto> categories;
    private List<ProductAttributes> attributes;
}
