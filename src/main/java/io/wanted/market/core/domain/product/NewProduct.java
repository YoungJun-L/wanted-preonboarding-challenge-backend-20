package io.wanted.market.core.domain.product;

import java.math.BigDecimal;

public record NewProduct(
        String name,
        BigDecimal price,
        Long quantity
) {
}
