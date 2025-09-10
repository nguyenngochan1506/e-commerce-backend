package dev.edu.ngochandev.productservice.entity;

import dev.edu.ngochandev.sharedkernel.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_option_values", schema = "product_schema")
@Getter
@Setter
public class OptionValueEntity extends BaseEntity {
    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private ProductOptionEntity option;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<ProductVariantEntity> productVariant = new HashSet<>();
}
