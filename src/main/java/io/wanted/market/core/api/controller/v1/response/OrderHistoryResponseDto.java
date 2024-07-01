package io.wanted.market.core.api.controller.v1.response;

import io.wanted.market.core.domain.order.OrderHistory;
import io.wanted.market.core.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderHistoryResponseDto(
        Long orderId,
        Long buyerId,
        String buyerUsername,
        BigDecimal price,
        OrderStatus status,
        LocalDateTime createdAt
) {
    public static OrderHistoryResponseDto from(OrderHistory orderHistory) {
        return new OrderHistoryResponseDto(
                orderHistory.orderId(),
                orderHistory.buyerId(),
                orderHistory.buyerUsername(),
                orderHistory.price(),
                orderHistory.status(),
                orderHistory.createdAt()
        );
    }
}
