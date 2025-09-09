package dev.edu.ngochandev.productservice.dto.req;

import dev.edu.ngochandev.productservice.common.MyCurrency;
import dev.edu.ngochandev.productservice.common.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Builder
@Getter
@Jacksonized
public class UpdateProductVariantRequestDto {
    private String sku;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private Double weight;
    private String dimensions;
    private String unit;
    private ProductStatus status;
    private MyCurrency currency;
    private Integer stock;
    private Boolean isDefault;
}
