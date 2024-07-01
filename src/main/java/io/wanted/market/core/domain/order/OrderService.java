package io.wanted.market.core.domain.order;

import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderHistoryReader orderHistoryReader;

    public List<OrderHistory> retrieveOrderHistories(User user, Product product) {
        return orderHistoryReader.read(user, product);
    }
}
