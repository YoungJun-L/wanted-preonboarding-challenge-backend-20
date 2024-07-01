package io.wanted.market.core.api.controller.v1.response;

import io.wanted.market.core.domain.order.OrderHistory;
import io.wanted.market.core.domain.product.Product;

import java.util.List;

public record ProductDetailsResponseDto(
        ProductResponseDto product,
        List<OrderHistoryResponseDto> histories
) {
    public static ProductDetailsResponseDto of(Product product, List<OrderHistory> orderHistories) {
        return new ProductDetailsResponseDto(
                ProductResponseDto.from(product),
                orderHistories.stream().map(OrderHistoryResponseDto::from).toList()
        );
    }
}
