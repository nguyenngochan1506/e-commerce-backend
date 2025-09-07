package dev.edu.ngochandev.productservice.repository;

import dev.edu.ngochandev.productservice.entity.ProductOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, String> {
}
