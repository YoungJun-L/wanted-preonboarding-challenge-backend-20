package io.wanted.market.core.api.controller.v1.response;

import io.wanted.market.core.domain.product.Product;

public record RegisterProductResponseDto(
        Long productId
) {
    public static RegisterProductResponseDto from(Product product) {
        return new RegisterProductResponseDto(product.id());
    }
}
