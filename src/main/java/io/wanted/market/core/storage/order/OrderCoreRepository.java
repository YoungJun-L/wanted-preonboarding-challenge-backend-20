package io.wanted.market.core.storage.order;

import io.wanted.market.core.domain.order.OrderHistory;
import io.wanted.market.core.domain.order.OrderRepository;
import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderCoreRepository implements OrderRepository {
    private final OrderHistoryJpaRepository orderHistoryJpaRepository;

    @Override
    public List<OrderHistory> readHistories(Product product) {
        return orderHistoryJpaRepository.findAllByProductIdOrderByCreatedAtDesc(product.id()).stream()
                .map(OrderHistoryEntity::toOrderHistory)
                .toList();
    }

    @Override
    public List<OrderHistory> readUserHistories(User user, Product product) {
        return orderHistoryJpaRepository.findAllByBuyerIdAndProductIdOrderByCreatedAtDesc(user.id(), product.id()).stream()
                .map(OrderHistoryEntity::toOrderHistory)
                .toList();
    }
}
