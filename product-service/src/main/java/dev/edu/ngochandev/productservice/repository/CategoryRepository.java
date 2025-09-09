package dev.edu.ngochandev.productservice.repository;

import dev.edu.ngochandev.productservice.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    boolean existsByName(String name);

    boolean existsBySlug(String slug);

    List<CategoryEntity> findAllByLevel( Integer level);
}
