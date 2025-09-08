package dev.edu.ngochandev.productservice.dto.res;

import dev.edu.ngochandev.productservice.common.MyCurrency;
import dev.edu.ngochandev.productservice.common.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class ProductVariantResponseDto {
    private String id;
    private String sku;
    private String name;
    private String imageUrl;
    private Double weight;
    private String dimensions;
    private String unit;
    private Integer stock;
    private BigDecimal price;
    private MyCurrency currency;
    private ProductStatus status;
}
