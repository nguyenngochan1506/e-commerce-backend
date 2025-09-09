package dev.edu.ngochandev.productservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductVariantEntity productVariant;
}
