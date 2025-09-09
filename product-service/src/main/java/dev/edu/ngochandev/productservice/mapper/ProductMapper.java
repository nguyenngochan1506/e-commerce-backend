package dev.edu.ngochandev.productservice.mapper;

import dev.edu.ngochandev.productservice.dto.req.CreateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.req.CreateProductVariantRequestDto;
import dev.edu.ngochandev.productservice.dto.res.*;
import dev.edu.ngochandev.productservice.entity.OptionValueEntity;
import dev.edu.ngochandev.productservice.entity.ProductEntity;
import dev.edu.ngochandev.productservice.entity.ProductOptionEntity;
import dev.edu.ngochandev.productservice.entity.ProductVariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    ProductEntity toProductEntity(CreateProductRequestDto req);

    @Mapping(source = "sku", target = "skuCode")
    ProductVariantEntity toProductVariantEntity(CreateProductVariantRequestDto req);

    ProductResponseDto toProductResponseDto(ProductEntity entity);

    @Mapping(source = "skuCode", target = "sku")
    @Mapping(source = "optionValues", target = "optionValueIds")
    ProductVariantResponseDto toProductVariantResponseDto(ProductVariantEntity entity);

    ProductDetailResponse toProductDetailResponse(ProductEntity entity);

    ProductOptionResponse toProductOptionResponse(ProductOptionEntity optionEntity);
    ProductOptionValueResponseDto toOptionValueResponseDto(OptionValueEntity optionValueEntity);

    default List<String> mapOptionValuesToIds(Set<OptionValueEntity> optionValues) {
        if (optionValues == null || optionValues.isEmpty()) {
            return Collections.emptyList();
        }
        return optionValues.stream()
                .map(OptionValueEntity::getId)
                .toList();
    }

    @Mapping(source = "variants", target = "defaultVariant")
    ProductListResponseDto toProductListResponseDto(ProductEntity productEntity);
    default ProductVariantResponseDto mapDefaultVariant(Set<ProductVariantEntity> variants) {
        if (variants == null || variants.isEmpty()) {
            return null;
        }
        return variants.stream()
                .filter(ProductVariantEntity::getIsDefault)
                .findFirst()
                .map(this::toProductVariantResponseDto)
                .orElse(null);
    }
}
