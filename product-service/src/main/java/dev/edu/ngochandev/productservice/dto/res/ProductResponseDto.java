package dev.edu.ngochandev.productservice.dto.res;

import dev.edu.ngochandev.productservice.common.ProductAttributes;
import dev.edu.ngochandev.productservice.common.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProductResponseDto {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String thumbnail;
    private ProductStatus status;
    private CategoryResponseDto category;
    private List<ProductAttributes> attributes;
}
