package io.wanted.market.core.domain.order;

import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository {
    List<OrderHistory> readHistories(Product product);

    List<OrderHistory> readUserHistories(User user, Product product);

    Order write(User user, NewOrder newOrder, Product product);
}
