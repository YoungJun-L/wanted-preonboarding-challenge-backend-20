package io.wanted.market.core.api.controller.v1.request;

import io.wanted.market.core.domain.order.NewOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequestDto(
        @NotNull
        @Positive
        Long productId
) {
    public NewOrder toNewOrder() {
        return new NewOrder(productId);
    }
}
