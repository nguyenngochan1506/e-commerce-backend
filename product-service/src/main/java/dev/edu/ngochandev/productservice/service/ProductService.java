package dev.edu.ngochandev.productservice.service;

import dev.edu.ngochandev.productservice.dto.req.CreateProductOptionRequestDto;
import dev.edu.ngochandev.productservice.dto.req.CreateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.res.ProductOptionResponseDto;
import dev.edu.ngochandev.productservice.dto.res.ProductResponseDto;

public interface ProductService {
    ProductOptionResponseDto createProductOption(CreateProductOptionRequestDto req);

    ProductResponseDto createProduct( CreateProductRequestDto req);
}
