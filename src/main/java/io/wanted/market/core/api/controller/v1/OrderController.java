package io.wanted.market.core.api.controller.v1;

import io.wanted.market.core.api.controller.v1.request.OrderRequestDto;
import io.wanted.market.core.api.controller.v1.response.OrderResponseDto;
import io.wanted.market.core.api.support.response.ApiResponse;
import io.wanted.market.core.domain.order.Order;
import io.wanted.market.core.domain.order.OrderService;
import io.wanted.market.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public ApiResponse<OrderResponseDto> order(
            User user,
            @RequestBody OrderRequestDto request
    ) {
        Order order = orderService.createOrder(user, request.toNewOrder());
        return ApiResponse.success(OrderResponseDto.from(order));
    }
}
