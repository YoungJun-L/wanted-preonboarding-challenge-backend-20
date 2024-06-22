package io.wanted.market.core.domain.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Product(
        Long id,
        Long sellerId,
        String name,
        BigDecimal price,
        Long quantity,
        LocalDateTime registeredAt
) {
}
