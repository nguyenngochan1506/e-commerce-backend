package dev.edu.ngochandev.productservice.entity;

import dev.edu.ngochandev.sharedkernel.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_categories", schema = "product_schema")
@Getter
@Setter
@SQLRestriction("is_deleted = false")
public class CategoryEntity extends BaseEntity {
    @Column(name = "name", nullable = false, length = 255, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "slug", nullable = false, length = 255, unique = true)
    private String slug;

    @Column(name = "thumbnail", length = 255)
    private String thumbnail;

    @Column(name = "level")
    private Integer level;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryEntity> children = new HashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<ProductEntity> products = new HashSet<>();


    @Override
    public void prePersist() {
        super.prePersist();
        if (isActive == null) {
            isActive = true; // default value
        }
        if (level == null) {
            level = (parent == null) ? 0 : parent.getLevel() + 1;
        }
    }

    public void addChild(CategoryEntity child) {
        children.add(child);
        child.setParent(this);
    }
    public void removeChild(CategoryEntity child) {
        children.remove(child);
        child.setParent(null);
    }
}
