package dev.edu.ngochandev.productservice.mapper;

import dev.edu.ngochandev.productservice.dto.req.CreateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.req.CreateProductVariantRequestDto;
import dev.edu.ngochandev.productservice.dto.res.ProductResponseDto;
import dev.edu.ngochandev.productservice.dto.res.ProductVariantResponseDto;
import dev.edu.ngochandev.productservice.entity.ProductEntity;
import dev.edu.ngochandev.productservice.entity.ProductVariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    ProductEntity toProductEntity(CreateProductRequestDto req);

    @Mapping(source = "sku", target = "skuCode")
    ProductVariantEntity toProductVariantEntity(CreateProductVariantRequestDto req);

    ProductResponseDto toProductResponseDto(ProductEntity entity);

    @Mapping(source = "skuCode", target = "sku")
    ProductVariantResponseDto toProductVariantResponseDto(ProductVariantEntity entity);


}
