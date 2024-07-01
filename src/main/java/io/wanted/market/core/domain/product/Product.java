package io.wanted.market.core.domain.product;

import io.wanted.market.core.domain.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record Product(
        Long id,
        Long sellerId,
        String name,
        BigDecimal price,
        Long totalQuantity,
        Long stockQuantity,
        LocalDateTime registeredAt,
        LocalDateTime updatedAt,
        ProductStatus status
) {
    public Long updatedAtTimeStamp() {
        return updatedAt.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    public boolean isSeller(User user) {
        return sellerId.equals(user.id());
    }
}
