package io.wanted.market.core.domain.order;

import io.wanted.market.ContextTest;
import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.product.ProductState;
import io.wanted.market.core.domain.user.User;
import io.wanted.market.core.domain.user.UserInfo;
import io.wanted.market.core.storage.order.OrderHistoryEntity;
import io.wanted.market.core.storage.order.OrderHistoryJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest extends ContextTest {
    private final OrderService orderService;

    private final OrderHistoryJpaRepository orderHistoryJpaRepository;

    OrderServiceTest(
            OrderService orderService,
            OrderHistoryJpaRepository orderHistoryJpaRepository
    ) {
        this.orderService = orderService;
        this.orderHistoryJpaRepository = orderHistoryJpaRepository;
    }

    @AfterEach
    void tearDown() {
        orderHistoryJpaRepository.deleteAllInBatch();
    }

    @DisplayName("유저가 제품의 구매자면 제품에서 해당 유저의 거래 내역을 조회한다.")
    @Test
    void retrieveOrderHistoriesWithBuyer() {
        // given
        User buyerA = new User(1L, new UserInfo("buyerA"));
        User buyerB = new User(2L, new UserInfo("buyerB"));
        User seller = new User(3L, new UserInfo("seller"));

        Product product = new Product(
                1L,
                seller.id(),
                seller.username(),
                BigDecimal.TEN,
                10L,
                10L,
                LocalDateTime.of(2023, 6, 23, 15, 29),
                LocalDateTime.of(2023, 6, 23, 15, 29),
                ProductState.AVAILABLE
        );

        List<OrderHistoryEntity> orderHistoryEntities = List.of(
                new OrderHistoryEntity(
                        1L,
                        buyerA.id(),
                        buyerA.username(),
                        seller.id(),
                        seller.username(),
                        product.id(),
                        product.name(),
                        BigDecimal.TEN,
                        OrderStatus.APPROVED
                ),
                new OrderHistoryEntity(
                        2L,
                        buyerB.id(),
                        buyerB.username(),
                        seller.id(),
                        seller.username(),
                        product.id(),
                        product.name(),
                        BigDecimal.TEN,
                        OrderStatus.APPROVED
                )
        );
        orderHistoryJpaRepository.saveAll(orderHistoryEntities);

        // when
        List<OrderHistory> orderHistories = orderService.retrieveOrderHistories(buyerA, product);

        // then
        assertThat(orderHistories).hasSize(1);
        assertThat(orderHistories.get(0).buyerId()).isEqualTo(buyerA.id());
    }

    @DisplayName("유저가 제품의 판매자면 제품의 전체 거래 내역을 조회한다.")
    @Test
    void retrieveOrderHistoriesWithSeller() {
        // given
        User buyerA = new User(1L, new UserInfo("buyerA"));
        User buyerB = new User(2L, new UserInfo("buyerB"));
        User seller = new User(3L, new UserInfo("seller"));

        Product product = new Product(
                1L,
                seller.id(),
                seller.username(),
                BigDecimal.TEN,
                10L,
                10L,
                LocalDateTime.of(2023, 6, 23, 15, 29),
                LocalDateTime.of(2023, 6, 23, 15, 29),
                ProductState.AVAILABLE
        );

        List<OrderHistoryEntity> orderHistoryEntities = List.of(
                new OrderHistoryEntity(
                        1L,
                        buyerA.id(),
                        buyerA.username(),
                        seller.id(),
                        seller.username(),
                        product.id(),
                        product.name(),
                        BigDecimal.TEN,
                        OrderStatus.APPROVED
                ),
                new OrderHistoryEntity(
                        2L,
                        buyerB.id(),
                        buyerB.username(),
                        seller.id(),
                        seller.username(),
                        product.id(),
                        product.name(),
                        BigDecimal.TEN,
                        OrderStatus.APPROVED
                )
        );
        orderHistoryJpaRepository.saveAll(orderHistoryEntities);

        // when
        List<OrderHistory> orderHistories = orderService.retrieveOrderHistories(seller, product);

        // then
        assertThat(orderHistories).hasSize(2);
    }
}
