package dev.edu.ngochandev.productservice.service;

import dev.edu.ngochandev.productservice.dto.req.CreateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.req.CreateProductVariantRequestDto;
import dev.edu.ngochandev.productservice.dto.res.ProductResponseDto;
import dev.edu.ngochandev.productservice.dto.res.ProductVariantResponseDto;

public interface ProductService {
    ProductResponseDto createProduct( CreateProductRequestDto req);

    ProductVariantResponseDto createProductVariant(String productId,CreateProductVariantRequestDto req);
}
