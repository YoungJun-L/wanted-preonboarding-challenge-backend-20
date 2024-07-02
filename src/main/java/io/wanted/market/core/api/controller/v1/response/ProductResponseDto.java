package io.wanted.market.core.api.controller.v1.response;

import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.product.ProductState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProductResponseDto(
        Long productId,
        Long sellerId,
        String name,
        BigDecimal price,
        Long totalQuantity,
        Long stockQuantity,
        LocalDateTime registeredAt,
        LocalDateTime updatedAt,
        ProductState state
) {
    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(
                product.id(),
                product.sellerId(),
                product.name(),
                product.price(),
                product.totalQuantity(),
                product.stockQuantity(),
                product.registeredAt(),
                product.updatedAt(),
                product.state()
        );
    }

    public static List<ProductResponseDto> list(List<Product> products) {
        return products.stream()
                .map(ProductResponseDto::from)
                .toList();
    }
}
