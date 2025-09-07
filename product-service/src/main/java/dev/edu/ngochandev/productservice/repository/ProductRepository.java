package dev.edu.ngochandev.productservice.repository;

import dev.edu.ngochandev.productservice.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    boolean existsBySlug(String slug);
}
