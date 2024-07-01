package io.wanted.market.core.storage.order;

import io.wanted.market.core.domain.order.OrderStatus;
import io.wanted.market.core.storage.support.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class OrderEntity extends BaseEntity {
    private Long buyerId;

    private Long productId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public OrderEntity(Long buyerId, Long productId) {
        this.buyerId = buyerId;
        this.productId = productId;
        this.orderStatus = OrderStatus.RESERVED;
    }
}
