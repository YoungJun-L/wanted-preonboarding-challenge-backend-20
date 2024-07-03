package io.wanted.market.core.domain.product;

import io.wanted.market.ContextTest;
import io.wanted.market.core.domain.support.cursor.Cursor;
import io.wanted.market.core.domain.support.cursor.SortType;
import io.wanted.market.core.domain.user.User;
import io.wanted.market.core.storage.product.ProductEntity;
import io.wanted.market.core.storage.product.ProductJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
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
    void registerProducts() {
        // given
        NewProduct newProduct = new NewProduct("product", BigDecimal.TEN, 100L);
        User user = new User(1L, null);

        // when
        Product product = productService.createProduct(user, newProduct);

        // then
        Optional<ProductEntity> actual = productJpaRepository.findById(product.id());
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getId()).isEqualTo(product.id());
    }

    @DisplayName("제품 조회 성공")
    @Test
    void retrieveProducts() {
        // given
        List<ProductEntity> productEntities = buildProductEntities();
        productJpaRepository.saveAll(productEntities);
        Cursor cursor = new Cursor(1L, 2L, ProductSortKey.UPDATED_AT, SortType.DESC);

        // when
        List<Product> products = productService.retrieveProducts(cursor);

        // then
        assertThat(products).hasSize(2);
    }

    private List<ProductEntity> buildProductEntities() {
        return List.of(
                new ProductEntity(1L, "productA", BigDecimal.valueOf(15_000), 10L, 10L),
                new ProductEntity(1L, "productB", BigDecimal.valueOf(5_000), 20L, 20L),
                new ProductEntity(2L, "productC", BigDecimal.valueOf(200_000), 100L, 100L)
        );
    }

    @DisplayName("커서 입력 시 다음 페이지가 조회된다.")
    @Test
    void retrieveProductsWithCursor() {
        // given
        List<ProductEntity> productEntities = buildProductEntities();
        productJpaRepository.saveAll(productEntities);

        List<ProductEntity> sorted = productEntities.stream()
                .sorted(Comparator.comparing(ProductEntity::getPrice).reversed())
                .toList();
        Cursor cursor = new Cursor(
                sorted.get(0).getPrice().longValue(),
                1L,
                ProductSortKey.PRICE,
                SortType.DESC
        );

        // when
        List<Product> products = productService.retrieveProducts(cursor);

        // then
        assertThat(products).hasSize(1);
        assertThat(products.get(0).id()).isEqualTo(sorted.get(1).getId());
    }
}
