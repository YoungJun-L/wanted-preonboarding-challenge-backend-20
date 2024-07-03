package io.wanted.market.core.domain.product;

import io.wanted.market.core.domain.support.error.CoreErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    @DisplayName("판매자가 제품 구매 시 실패한다.")
    @Test
    void purchaseWithSeller() {
        // given
        Product product = new Product(
                1L,
                1L,
                "수박",
                BigDecimal.valueOf(20_000),
                100L,
                100L,
                LocalDateTime.of(2024, 6, 23, 15, 29),
                LocalDateTime.of(2024, 6, 23, 15, 29),
                ProductState.AVAILABLE
        );

        // when & then
        assertThatThrownBy(() -> product.purchased(1L))
                .hasFieldOrPropertyWithValue("coreErrorType", CoreErrorType.FORBIDDEN_ERROR);
    }

    @DisplayName("제품 구매 시 재고가 1 감소한다.")
    @Test
    void purchaseShouldDecreaseStock() {
        // given
        Product product = new Product(
                1L,
                1L,
                "수박",
                BigDecimal.valueOf(20_000),
                100L,
                100L,
                LocalDateTime.of(2024, 6, 23, 15, 29),
                LocalDateTime.of(2024, 6, 23, 15, 29),
                ProductState.AVAILABLE
        );

        // when
        Product purchasedProduct = product.purchased(2L);

        // then
        assertThat(purchasedProduct.stockQuantity()).isEqualTo(product.stockQuantity() - 1);
    }

    @DisplayName("제품 구매 후 재고가 없으면 상태가 재고 없음으로 변한다.")
    @Test
    void purchaseBeforeOutOfStockShouldChangeState() {
        // given
        Product product = new Product(
                1L,
                1L,
                "수박",
                BigDecimal.valueOf(20_000),
                100L,
                1L,
                LocalDateTime.of(2024, 6, 23, 15, 29),
                LocalDateTime.of(2024, 6, 23, 15, 29),
                ProductState.AVAILABLE
        );

        // when
        Product purchasedProduct = product.purchased(2L);

        // then
        assertThat(purchasedProduct.state()).isEqualTo(ProductState.OUT_OF_STOCK);
    }

    @DisplayName("제품 구매 시 재고가 없으면 실패한다.")
    @Test
    void purchaseWhenOutOfStock() {
        // given
        Product product = new Product(
                1L,
                1L,
                "수박",
                BigDecimal.valueOf(20_000),
                100L,
                0L,
                LocalDateTime.of(2024, 6, 23, 15, 29),
                LocalDateTime.of(2024, 6, 23, 15, 29),
                ProductState.AVAILABLE
        );

        // when & then
        assertThatThrownBy(() -> product.purchased(2L))
                .hasFieldOrPropertyWithValue("coreErrorType", CoreErrorType.PRODUCT_OUT_OF_STOCK_ERROR);
    }
}
