package io.wanted.market.core.domain.order;

import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.product.ProductReader;
import io.wanted.market.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderWriter {
    private final OrderRepository orderRepository;

    private final ProductReader productReader;

    public Order write(User user, NewOrder newOrder) {
        Product product = productReader.read(newOrder.productId());
        return orderRepository.write(user, newOrder, product.purchased(user.id()));
    }
}
