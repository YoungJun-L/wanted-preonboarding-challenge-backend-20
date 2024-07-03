package io.wanted.market.core.domain.order;

import io.wanted.market.ContextTest;
import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.product.ProductState;
import io.wanted.market.core.domain.user.User;
import io.wanted.market.core.domain.user.UserInfo;
import io.wanted.market.core.storage.order.OrderHistoryEntity;
import io.wanted.market.core.storage.order.OrderHistoryJpaRepository;
import io.wanted.market.core.storage.order.OrderJpaRepository;
import io.wanted.market.core.storage.product.ProductEntity;
import io.wanted.market.core.storage.product.ProductJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderServiceTest extends ContextTest {
    private final OrderService orderService;

    private final OrderJpaRepository orderJpaRepository;

    private final OrderHistoryJpaRepository orderHistoryJpaRepository;

    private final ProductJpaRepository productJpaRepository;

    OrderServiceTest(
            OrderService orderService,
            OrderJpaRepository orderJpaRepository,
            OrderHistoryJpaRepository orderHistoryJpaRepository,
            ProductJpaRepository productJpaRepository
    ) {
        this.orderService = orderService;
        this.orderJpaRepository = orderJpaRepository;
        this.orderHistoryJpaRepository = orderHistoryJpaRepository;
        this.productJpaRepository = productJpaRepository;
    }

    @AfterEach
    void tearDown() {
        orderJpaRepository.deleteAllInBatch();
        orderHistoryJpaRepository.deleteAllInBatch();
        productJpaRepository.deleteAllInBatch();
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
                "수박",
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
                "수박",
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

    @DisplayName("주문 성공")
    @Test
    void createOrder() {
        // given
        ProductEntity productEntity = new ProductEntity(
                2L,
                "수박",
                BigDecimal.valueOf(1_000),
                10L,
                10L
        );
        productJpaRepository.save(productEntity);

        User buyer = new User(1L, new UserInfo("buyer"));
        NewOrder newOrder = new NewOrder(productEntity.getId());

        // when
        Order actual = orderService.createOrder(buyer, newOrder);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.buyerId()).isEqualTo(buyer.id());
            softly.assertThat(actual.productId()).isEqualTo(productEntity.getId());
            softly.assertThat(actual.status()).isEqualTo(OrderStatus.NEW);
        });
    }

    @DisplayName("주문 성공 시 거래 내역이 생성된다.")
    @Test
    void createOrderShouldCreateHistory() {
        // given
        ProductEntity productEntity = new ProductEntity(
                2L,
                "수박",
                BigDecimal.valueOf(1_000),
                10L,
                10L
        );
        productJpaRepository.save(productEntity);

        User buyer = new User(1L, new UserInfo("buyer"));
        NewOrder newOrder = new NewOrder(productEntity.getId());

        // when
        Order order = orderService.createOrder(buyer, newOrder);

        // then
        List<OrderHistoryEntity> actual = orderHistoryJpaRepository.findAllByProductIdOrderByCreatedAtDesc(productEntity.getId());
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            softly.assertThat(actual.get(0).getOrderId()).isEqualTo(order.id());
            softly.assertThat(actual.get(0).getBuyerId()).isEqualTo(buyer.id());
            softly.assertThat(actual.get(0).getBuyerUsername()).isEqualTo(buyer.username());
            softly.assertThat(actual.get(0).getSellerId()).isEqualTo(productEntity.getSellerId());
            softly.assertThat(actual.get(0).getProductId()).isEqualTo(productEntity.getId());
            softly.assertThat(actual.get(0).getProductName()).isEqualTo(productEntity.getName());
            softly.assertThat(actual.get(0).getPrice()).isEqualByComparingTo(productEntity.getPrice());
            softly.assertThat(actual.get(0).getStatus()).isEqualTo(order.status());
        });
    }

    @DisplayName("주문 성공 시 제품의 상태가 변경된다.")
    @Test
    void createOrderShouldDecreaseStock() {
        // given
        ProductEntity productEntity = new ProductEntity(
                2L,
                "수박",
                BigDecimal.valueOf(1_000),
                10L,
                10L
        );
        productJpaRepository.save(productEntity);

        User buyer = new User(1L, new UserInfo("buyer"));
        NewOrder newOrder = new NewOrder(productEntity.getId());

        // when
        orderService.createOrder(buyer, newOrder);

        // then
        ProductEntity actual = productJpaRepository.findById(productEntity.getId()).orElseThrow();
        assertSoftly(softly -> {
            softly.assertThat(actual.getSellerId()).isEqualTo(2L);
            softly.assertThat(actual.getName()).isEqualTo("수박");
            softly.assertThat(actual.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(1_000));
            softly.assertThat(actual.getTotalQuantity()).isEqualTo(10L);
            softly.assertThat(actual.getStockQuantity()).isEqualTo(9L);
            softly.assertThat(actual.getState()).isEqualTo(ProductState.AVAILABLE);
        });
    }
}
