package io.wanted.market.core.storage.order;

import io.wanted.market.core.domain.order.NewOrder;
import io.wanted.market.core.domain.order.Order;
import io.wanted.market.core.domain.order.OrderHistory;
import io.wanted.market.core.domain.order.OrderRepository;
import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.support.error.CoreErrorType;
import io.wanted.market.core.domain.support.error.CoreException;
import io.wanted.market.core.domain.user.User;
import io.wanted.market.core.storage.product.ProductEntity;
import io.wanted.market.core.storage.product.ProductJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderCoreRepository implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;

    private final OrderHistoryJpaRepository orderHistoryJpaRepository;

    private final ProductJpaRepository productJpaRepository;

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

    @Transactional
    @Override
    public Order write(User user, NewOrder newOrder, Product product) {
        OrderEntity savedOrderEntity = orderJpaRepository.save(
                new OrderEntity(
                        user.id(),
                        product.id()
                )
        );
        orderHistoryJpaRepository.save(
                new OrderHistoryEntity(
                        savedOrderEntity.getId(),
                        user.id(),
                        user.username(),
                        product.sellerId(),
                        product.id(),
                        product.name(),
                        product.price(),
                        savedOrderEntity.getStatus()
                )
        );
        ProductEntity productEntity = productJpaRepository.findById(product.id())
                .orElseThrow(() -> new CoreException(CoreErrorType.PRODUCT_NOT_FOUND_ERROR));
        productEntity.update(product);
        return savedOrderEntity.toOrder();
    }
}
