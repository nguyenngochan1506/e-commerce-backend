package dev.edu.ngochandev.productservice.mapper;

import dev.edu.ngochandev.productservice.dto.req.CreateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.res.ProductOptionResponseDto;
import dev.edu.ngochandev.productservice.dto.res.ProductResponseDto;
import dev.edu.ngochandev.productservice.entity.ProductEntity;
import dev.edu.ngochandev.productservice.entity.ProductOptionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    ProductOptionResponseDto toProductOptionResponseDto(ProductOptionEntity entity);

    ProductEntity toProductEntity(CreateProductRequestDto req);

    ProductResponseDto toProductResponseDto(ProductEntity entity);
}
