package dev.edu.ngochandev.productservice.repository;

import dev.edu.ngochandev.productservice.entity.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, String> {
    boolean existsBySkuCode(String skuCode);

    @Query("SELECT pv FROM ProductVariantEntity pv JOIN FETCH pv.optionValues JOIN pv.product p WHERE p.id = :id")
    List<ProductVariantEntity> findByProductId(@Param("id") String id);
}
