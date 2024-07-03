package io.wanted.market.core.api.controller.v1.response;

import io.wanted.market.core.domain.order.Order;

public record OrderResponseDto(Long orderId) {
    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(order.id());
    }
}
