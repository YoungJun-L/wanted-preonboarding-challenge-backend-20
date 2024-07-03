package io.wanted.market.core.storage.order;

import io.wanted.market.core.domain.order.OrderHistory;
import io.wanted.market.core.domain.order.OrderStatus;
import io.wanted.market.core.storage.support.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders_history")
@Entity
public class OrderHistoryEntity extends BaseEntity {
    private Long orderId;

    private Long buyerId;

    private String buyerUsername;

    private Long sellerId;

    private Long productId;

    private String productName;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public OrderHistoryEntity(
            Long orderId,
            Long buyerId,
            String buyerUsername,
            Long sellerId,
            Long productId,
            String productName,
            BigDecimal price,
            OrderStatus status
    ) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.buyerUsername = buyerUsername;
        this.sellerId = sellerId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.status = status;
    }

    public OrderHistory toOrderHistory() {
        return new OrderHistory(
                getId(),
                orderId,
                buyerId,
                buyerUsername,
                sellerId,
                productId,
                productName,
                price,
                status,
                getCreatedAt()
        );
    }
}
