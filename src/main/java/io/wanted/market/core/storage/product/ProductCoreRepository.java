package io.wanted.market.core.storage.product;

import io.wanted.market.core.domain.product.NewProduct;
import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.product.ProductRepository;
import io.wanted.market.core.domain.product.ProductSortKey;
import io.wanted.market.core.domain.support.cursor.Cursor;
import io.wanted.market.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductCoreRepository implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    private final ProductQueryRepository productQueryRepository;

    @Override
    public Product write(User user, NewProduct newProduct) {
        ProductEntity savedProductEntity = productJpaRepository.save(
                new ProductEntity(
                        user.id(),
                        newProduct.name(),
                        newProduct.price(),
                        newProduct.quantity(),
                        newProduct.quantity()
                )
        );
        return savedProductEntity.toProduct();
    }

    @Override
    public List<Product> read(Cursor cursor) {
        List<ProductEntity> productEntities = Collections.emptyList();
        if (cursor.sortKey() == ProductSortKey.UPDATED_AT) {
            productEntities = productQueryRepository.findAllOnSaleOrderByUpdatedAt(cursor.cursor(), cursor.limit(), cursor.sortType());
        } else if (cursor.sortKey() == ProductSortKey.PRICE) {
            productEntities = productQueryRepository.findAllOnSaleOrderByPrice(cursor.cursor(), cursor.limit(), cursor.sortType());
        }
        return productEntities.stream()
                .map(ProductEntity::toProduct)
                .toList();
    }

    @Override
    public Optional<Product> read(Long productId) {
        return productJpaRepository.findById(productId).map(ProductEntity::toProduct);
    }
}
