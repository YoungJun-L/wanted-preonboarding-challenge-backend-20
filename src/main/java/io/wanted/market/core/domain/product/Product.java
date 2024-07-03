package io.wanted.market.core.domain.product;

import io.wanted.market.core.domain.support.error.CoreErrorType;
import io.wanted.market.core.domain.support.error.CoreException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Product(
        Long id,
        Long sellerId,
        String name,
        BigDecimal price,
        Long totalQuantity,
        Long stockQuantity,
        LocalDateTime registeredAt,
        LocalDateTime updatedAt,
        ProductState state
) {
    public Product purchased(Long userId) {
        verifyCanPurchase(userId);
        return new Product(
                id,
                sellerId,
                name,
                price,
                totalQuantity,
                stockQuantity - 1,
                registeredAt,
                updatedAt,
                state.changedByStock(stockQuantity - 1)
        );
    }

    private void verifyCanPurchase(Long userId) {
        if (isSeller(userId)) {
            throw new CoreException(CoreErrorType.FORBIDDEN_ERROR);
        }

        if (stockQuantity == 0) {
            throw new CoreException(CoreErrorType.PRODUCT_OUT_OF_STOCK_ERROR);
        }
    }

    public boolean isSeller(Long userId) {
        return sellerId.equals(userId);
    }
}
