package dev.edu.ngochandev.productservice.dto.res;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class ProductListResponseDto extends ProductResponseDto{
    private ProductVariantResponseDto defaultVariant;
}
