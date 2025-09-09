package dev.edu.ngochandev.productservice.service;

import dev.edu.ngochandev.productservice.dto.req.CreateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.req.CreateProductVariantRequestDto;
import dev.edu.ngochandev.productservice.dto.req.UpdateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.res.ProductDetailResponse;
import dev.edu.ngochandev.productservice.dto.res.ProductListResponseDto;
import dev.edu.ngochandev.productservice.dto.res.ProductResponseDto;
import dev.edu.ngochandev.productservice.dto.res.ProductVariantResponseDto;
import dev.edu.ngochandev.sharedkernel.dto.res.PageResponseDto;

public interface ProductService {
    ProductResponseDto createProduct( CreateProductRequestDto req);

    ProductVariantResponseDto createProductVariant(String productId,CreateProductVariantRequestDto req);

    ProductDetailResponse getProductDetail(String productId);

    PageResponseDto<ProductListResponseDto> getListProducts(Integer page, Integer size, String sort, String search);

    ProductResponseDto updateProduct( UpdateProductRequestDto req);
}
