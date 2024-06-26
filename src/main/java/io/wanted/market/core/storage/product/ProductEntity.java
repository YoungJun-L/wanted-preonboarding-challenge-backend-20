package io.wanted.market.core.storage.product;

import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.product.ProductStatus;
import io.wanted.market.core.storage.support.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
public class ProductEntity extends BaseEntity {
    private Long sellerId;

    private String name;

    private BigDecimal price;

    private Long totalQuantity;

    private Long stockQuantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public ProductEntity(Long sellerId, String name, BigDecimal price, Long totalQuantity) {
        this.sellerId = sellerId;
        this.name = name;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.stockQuantity = totalQuantity;
        this.status = ProductStatus.SALE;
    }

    public Product toProduct() {
        return new Product(
                getId(),
                sellerId,
                name,
                price,
                totalQuantity,
                stockQuantity,
                getCreatedAt(),
                getUpdatedAt(),
                status
        );
    }
}
