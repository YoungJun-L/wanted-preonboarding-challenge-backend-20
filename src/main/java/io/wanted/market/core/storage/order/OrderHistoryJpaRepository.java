package io.wanted.market.core.storage.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryJpaRepository extends JpaRepository<OrderHistoryEntity, Long> {
    List<OrderHistoryEntity> findAllByProductIdOrderByCreatedAtDesc(Long productId);

    List<OrderHistoryEntity> findAllByBuyerIdAndProductIdOrderByCreatedAtDesc(Long buyerId, Long productId);
}
