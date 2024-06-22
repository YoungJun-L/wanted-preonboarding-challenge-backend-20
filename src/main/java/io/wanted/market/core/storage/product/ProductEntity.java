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

    private Long quantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public ProductEntity(Long sellerId, String name, BigDecimal price, Long quantity) {
        this.sellerId = sellerId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = ProductStatus.SALE;
    }

    public Product toProduct() {
        return new Product(getId(), sellerId, name, price, quantity, getCreatedAt(), status);
    }
}
