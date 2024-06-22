package io.wanted.market.core.domain.product;

import io.wanted.market.ContextTest;
import io.wanted.market.core.domain.user.User;
import io.wanted.market.core.storage.product.ProductEntity;
import io.wanted.market.core.storage.product.ProductJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest extends ContextTest {
    private final ProductService productService;

    private final ProductJpaRepository productJpaRepository;

    ProductServiceTest(
            ProductService productService,
            ProductJpaRepository productJpaRepository
    ) {
        this.productService = productService;
        this.productJpaRepository = productJpaRepository;
    }

    @AfterEach
    void tearDown() {
        productJpaRepository.deleteAllInBatch();
    }

    @DisplayName("제품 등록 성공")
    @Test
    void register() {
        // given
        NewProduct newProduct = new NewProduct("product", BigDecimal.TEN, 100L);
        User user = new User(1L, null);

        // when
        Product product = productService.register(user, newProduct);
        System.out.println(product.registeredAt());

        // then
        Optional<ProductEntity> actual = productJpaRepository.findById(product.id());
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getId()).isEqualTo(product.id());
    }
}
