package io.wanted.market.core.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderHistory(
        Long id,
        Long orderId,
        Long buyerId,
        String buyerUsername,
        Long sellerId,
        Long productId,
        String productName,
        BigDecimal price,
        OrderStatus status,
        LocalDateTime createdAt
) {
}
