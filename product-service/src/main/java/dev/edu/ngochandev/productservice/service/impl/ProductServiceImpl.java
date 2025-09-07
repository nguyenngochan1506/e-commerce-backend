package dev.edu.ngochandev.productservice.service.impl;

import dev.edu.ngochandev.productservice.dto.req.CreateProductOptionRequestDto;
import dev.edu.ngochandev.productservice.dto.req.CreateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.res.ProductOptionResponseDto;
import dev.edu.ngochandev.productservice.dto.res.ProductResponseDto;
import dev.edu.ngochandev.productservice.entity.ProductEntity;
import dev.edu.ngochandev.productservice.mapper.ProductMapper;
import dev.edu.ngochandev.productservice.repository.CategoryRepository;
import dev.edu.ngochandev.productservice.repository.ProductOptionRepository;
import dev.edu.ngochandev.productservice.repository.ProductRepository;
import dev.edu.ngochandev.productservice.service.ProductService;
import dev.edu.ngochandev.sharedkernel.exception.DuplicateResourceException;
import dev.edu.ngochandev.sharedkernel.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductOptionResponseDto createProductOption(CreateProductOptionRequestDto req) {
//        TODO: Implement this method
        return null;
    }

    @Override
    public ProductResponseDto createProduct(CreateProductRequestDto req) {
        //check category exists
        if(!categoryRepository.existsById(req.getCategoryId())) throw new ResourceNotFoundException("error.category.not-found");
        //check slug exists
        if(productRepository.existsBySlug((req.getSlug()))) throw new DuplicateResourceException("error.slug-exists");

        //map to entity
        ProductEntity product = productMapper.toProductEntity(req);
        product.setCategory(categoryRepository.findById(req.getCategoryId()).get());
        //save to db
        product = productRepository.save(product);

        return productMapper.toProductResponseDto(product);
    }
}
