package dev.edu.ngochandev.productservice.repository;

import dev.edu.ngochandev.productservice.common.ProductStatus;
import dev.edu.ngochandev.productservice.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    boolean existsBySlug(String slug);

    @Query("""
        SELECT p FROM ProductEntity p JOIN p.category c JOIN p.variants v 
                    WHERE (LOWER(p.name) LIKE CONCAT('%', LOWER(:search), '%') OR 
                    LOWER(p.slug) LIKE CONCAT('%', LOWER(:search), '%') OR 
                    LOWER(c.name) LIKE CONCAT('%', LOWER(:search), '%') OR 
                    LOWER(v.skuCode) LIKE CONCAT('%', LOWER(:search), '%'))
                    AND p.status = 'PUBLISHED' """)
    Page<ProductEntity> findProductWithSearch(@Param("search") String search, Pageable pageable);

    Page<ProductEntity> findAllByStatus(ProductStatus status, Pageable pageable);

    Optional<ProductEntity> findByIdAndStatus(String id, ProductStatus status);
}
