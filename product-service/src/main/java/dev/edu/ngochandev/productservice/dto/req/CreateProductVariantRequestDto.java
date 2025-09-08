package dev.edu.ngochandev.productservice.dto.req;

import dev.edu.ngochandev.productservice.common.MyCurrency;
import dev.edu.ngochandev.productservice.common.ProductOption;
import dev.edu.ngochandev.productservice.common.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Jacksonized
public class CreateProductVariantRequestDto {
    @NotBlank(message = "error.sku.not-blank")
    private String sku;

    @NotBlank(message = "error.name.not-blank")
    private String name;

    @NotNull(message = "error.price.not-blank")
    private BigDecimal price;

    @NotNull(message = "error.options.not-null")
    @NotEmpty(message = "error.options.not-empty")
    private List<ProductOption> options;

    @NotBlank(message = "error.product.image.not-blank")
    private String imageUrl;

    @NotNull(message = "error.weight.not-null")
    private Double weight;

    @NotBlank(message = "error.dimensions.not-blank")
    private String dimensions;

    @NotBlank(message = "error.unit.not-blank")
    private String unit;

    private ProductStatus status;
    private MyCurrency currency;
    private Integer stock;
    private Boolean isDefault;

}
