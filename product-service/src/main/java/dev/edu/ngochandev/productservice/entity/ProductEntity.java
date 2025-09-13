package dev.edu.ngochandev.productservice.entity;

import dev.edu.ngochandev.productservice.common.ProductAttributes;
import dev.edu.ngochandev.productservice.common.ProductStatus;
import dev.edu.ngochandev.sharedkernel.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.util.*;

@Entity
@Table(name = "tbl_products", schema = "product_schema")
@Getter
@Setter
@SQLRestriction("is_deleted = false")
public class ProductEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "attributes", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ProductAttributes> attributes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductVariantEntity> variants = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ProductOptionEntity> options = new HashSet<>();

    @Override
    public void prePersist() {
        super.prePersist();
        if (status == null) {
            status = ProductStatus.DRAFT;
        }
    }
}
