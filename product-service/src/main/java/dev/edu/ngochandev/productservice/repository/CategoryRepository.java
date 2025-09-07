package dev.edu.ngochandev.productservice.repository;

import dev.edu.ngochandev.productservice.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    boolean existsByName(String name);

    boolean existsBySlug(String slug);

    @Query("SELECT c FROM CategoryEntity c JOIN c.children WHERE c.level = :level")
    List<CategoryEntity> findAllByLevel(@Param("level") Integer level);
}
