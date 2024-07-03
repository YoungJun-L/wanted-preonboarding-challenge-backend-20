package io.wanted.market.core.domain.order;

import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderHistoryReader {
    private final OrderRepository orderRepository;

    public List<OrderHistory> read(User user, Product product) {
        if (product.isSeller(user.id())) {
            return orderRepository.readHistories(product);
        }
        return orderRepository.readUserHistories(user, product);
    }
}
