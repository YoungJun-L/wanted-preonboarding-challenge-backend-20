package io.wanted.market.core.domain.order;

import java.time.LocalDateTime;

public record Order(
        Long id,
        Long buyerId,
        Long productId,
        OrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
