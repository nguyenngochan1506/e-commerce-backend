package dev.edu.ngochandev.productservice.service.impl;

import dev.edu.ngochandev.productservice.common.MyUtils;
import dev.edu.ngochandev.productservice.common.ProductStatus;
import dev.edu.ngochandev.productservice.dto.req.CreateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.req.CreateProductVariantRequestDto;
import dev.edu.ngochandev.productservice.dto.req.UpdateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.res.*;
import dev.edu.ngochandev.productservice.entity.*;
import dev.edu.ngochandev.productservice.mapper.CategoryMapper;
import dev.edu.ngochandev.productservice.mapper.ProductMapper;
import dev.edu.ngochandev.productservice.repository.CategoryRepository;
import dev.edu.ngochandev.productservice.repository.ProductOptionRepository;
import dev.edu.ngochandev.productservice.repository.ProductRepository;
import dev.edu.ngochandev.productservice.repository.ProductVariantRepository;
import dev.edu.ngochandev.productservice.service.ProductService;
import dev.edu.ngochandev.sharedkernel.dto.res.PageResponseDto;
import dev.edu.ngochandev.sharedkernel.exception.DuplicateResourceException;
import dev.edu.ngochandev.sharedkernel.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponseDto createProduct(CreateProductRequestDto req) {
        //check category exists
        if(!categoryRepository.existsById(req.getCategoryId())) throw new ResourceNotFoundException("error.category.not-found");
        //check slug exists
        if(productRepository.existsBySlug((req.getSlug()))) throw new DuplicateResourceException("error.slug-exists");

        //map to entity
        ProductEntity product = productMapper.toProductEntity(req);
        CategoryEntity directCategory = categoryRepository.findById(req.getCategoryId()).get();
        product.setCategory(directCategory);
        //save to db
        product = productRepository.save(product);
        ProductResponseDto responseDto = productMapper.toProductResponseDto(product);
        responseDto.setCategories(buildCategoryBreadcrumb(directCategory));

        return responseDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductVariantResponseDto createProductVariant(String productId, CreateProductVariantRequestDto req) {
        ProductEntity product = this.findProductById(productId);

        if (productVariantRepository.existsBySkuCode(req.getSku())) {
            throw new DuplicateResourceException("error.sku-exists");
        }

        ProductVariantEntity variant = productMapper.toProductVariantEntity(req);
        variant.setProduct(product);

        ProductVariantEntity savedVariant = productVariantRepository.save(variant);

        if (req.getOptions() != null && !req.getOptions().isEmpty()) {

            for (var optionDto : req.getOptions()) {
                ProductOptionEntity optionEntity = productOptionRepository.findByProductIdAndName(product.getId(), optionDto.name())
                        .orElseGet(() -> {
                            ProductOptionEntity newOption = new ProductOptionEntity();
                            newOption.setName(optionDto.name());
                            newOption.setProduct(product);
                            return productOptionRepository.save(newOption);
                        });

                OptionValueEntity optionValueEntity = optionEntity.getValues().stream()
                        .filter(v -> v.getValue().equals(optionDto.value()))
                        .findFirst()
                        .orElseGet(() -> {
                            OptionValueEntity newValue = new OptionValueEntity();
                            newValue.setValue(optionDto.value());
                            newValue.setOption(optionEntity);
                            newValue.setProductVariant(variant);
                            return newValue;
                        });

                savedVariant.getOptionValues().add(optionValueEntity);
            }
        }

        if (Boolean.TRUE.equals(req.getIsDefault())) {
            productVariantRepository.findByProductId(product.getId()).forEach(v -> {
                if (!v.getId().equals(savedVariant.getId())) {
                    v.setIsDefault(false);
                }
            });
            savedVariant.setIsDefault(true);
        }

        ProductVariantEntity finalVariant = productVariantRepository.save(savedVariant);

        long defaultCount = productVariantRepository.findByProductId(product.getId()).stream()
                .filter(ProductVariantEntity::getIsDefault).count();

        if (defaultCount == 0) {
            finalVariant.setIsDefault(true);
            finalVariant = productVariantRepository.save(finalVariant);
        }


        return productMapper.toProductVariantResponseDto(finalVariant);
    }

    @Override
    public ProductDetailResponse getProductDetail(String productId) {
        ProductEntity product = this.findProductById(productId);

        ProductDetailResponse response = productMapper.toProductDetailResponse(product);
        response.setCategories(buildCategoryBreadcrumb(product.getCategory()));

        return response;
    }

    @Override
    public PageResponseDto<ProductListResponseDto> getListProducts(Integer page, Integer size, String sort, String search) {
        Pageable pageable = MyUtils.createPageable(page, size, sort);

        Page<ProductEntity> productPage;
        if(StringUtils.hasLength(search)) {
            productPage = productRepository.findProductWithSearch(search.toLowerCase(), pageable);
        } else {
            productPage = productRepository.findAllByStatus(ProductStatus.PUBLISHED, pageable);
        }

        List<ProductListResponseDto> productList = productPage.getContent().stream()
                .map(p -> {
                    ProductListResponseDto dto = productMapper.toProductListResponseDto(p);
                    dto.setCategories(buildCategoryBreadcrumb(p.getCategory()));
                    return dto;
                })
                .toList();

        return PageResponseDto.<ProductListResponseDto>builder()
                .totalPages(productPage.getTotalPages())
                .totalItems(productPage.getTotalElements())
                .currentPage(page)
                .items(productList)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponseDto updateProduct(UpdateProductRequestDto req) {
        ProductEntity productToUpdate = productRepository.findById(req.getId()).orElseThrow(() -> new ResourceNotFoundException("error.product-not-found"));

        //check slug
        if (StringUtils.hasText(req.getSlug()) && !productToUpdate.getSlug().equals(req.getSlug())) {
            if (productRepository.existsBySlug(req.getSlug())) {
                throw new DuplicateResourceException("error.slug-exists");
            }
            productToUpdate.setSlug(req.getSlug());
        }
        //check category
        if (StringUtils.hasText(req.getCategoryId()) && !productToUpdate.getCategory().getId().equals(req.getCategoryId())) {
            CategoryEntity newCategory = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("error.category.not-found"));
            productToUpdate.setCategory(newCategory);
        }
        //update other fields
        if (StringUtils.hasText(req.getName())) productToUpdate.setName(req.getName());
        if (StringUtils.hasText(req.getDescription())) productToUpdate.setDescription(req.getDescription());
        if (StringUtils.hasText(req.getThumbnail())) productToUpdate.setThumbnail(req.getThumbnail());
        if (req.getStatus() != null) productToUpdate.setStatus(req.getStatus());
        if (req.getAttributes() != null && !req.getAttributes().isEmpty()) productToUpdate.setAttributes(req.getAttributes());

        ProductEntity updatedProduct = productRepository.save(productToUpdate);

        ProductResponseDto responseDto = productMapper.toProductResponseDto(updatedProduct);
        responseDto.setCategories(buildCategoryBreadcrumb(updatedProduct.getCategory()));

        return responseDto;
    }

    private ProductEntity findProductById(String productId) {
        return productRepository.findByIdAndStatus(productId, ProductStatus.PUBLISHED).orElseThrow(() -> new ResourceNotFoundException("error.product.not-found"));
    }
    private List<CategoryResponseDto> buildCategoryBreadcrumb(CategoryEntity category) {
        List<CategoryEntity> path = categoryRepository.findCategoryPathToRoot(category.getId());

        Collections.reverse(path);

        return path.stream()
                .map(categoryMapper::toFlatCategoryResponseDto)
                .toList();
    }
}
