package dev.edu.ngochandev.productservice.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
public class ProductDetailResponse extends ProductResponseDto{
    private List<ProductOptionResponse> options;
    private List<ProductVariantResponseDto> variants;
}
