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

    List<CategoryEntity> findAllByLevel( Integer level);

    @Query(value = """
        WITH RECURSIVE category_path AS (
            SELECT c.*
            FROM product_schema.tbl_categories c
            WHERE c.id = :categoryId
            UNION ALL
            SELECT parent.*
            FROM product_schema.tbl_categories parent
            INNER JOIN category_path cp ON cp.parent_id = parent.id
        )
        SELECT * FROM category_path
        """, nativeQuery = true)
    List<CategoryEntity> findCategoryPathToRoot(@Param("categoryId") String categoryId);
}
