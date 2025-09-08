package dev.edu.ngochandev.productservice.repository;

import dev.edu.ngochandev.productservice.entity.ProductOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, String> {
    Optional<Object> findByName(String name);

    Optional<ProductOptionEntity> findByProductIdAndName(String productId, String name);
}
