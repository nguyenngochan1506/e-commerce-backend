package dev.edu.ngochandev.productservice.entity;


import dev.edu.ngochandev.productservice.common.MyCurrency;
import dev.edu.ngochandev.productservice.common.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_product_variants", schema = "product_schema")
@Getter
@Setter
public class ProductVariant extends BaseEntity{
    @Column(name = "sku_code", nullable = false, unique = true)
    private String skuCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "price", nullable = false, columnDefinition = "DECIMAL(15,2)")
    private BigDecimal price;

    @Column(name = "currency", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private MyCurrency currency;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "demension", nullable = false)
    private String demension;

    @Column(name = "unit", nullable = false )
    private String unit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tbl_products_option_values",
            schema = "product_schema",
            joinColumns = @JoinColumn(name = "product_variant_id"),
            inverseJoinColumns = @JoinColumn(name = "option_value_id")
    )
    private Set<OptionValueEntity> optionValues = new HashSet<>();

    @Override
    public void prePersist() {
        super.prePersist();
        if (status == null) {
            status = ProductStatus.DRAFT;
        }
        if (isDefault == null) {
            isDefault = false;
        }
        if(stock == null) {
            stock = 0;
        }
    }
}
