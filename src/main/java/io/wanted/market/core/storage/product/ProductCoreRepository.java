package io.wanted.market.core.storage.product;

import io.wanted.market.core.domain.product.NewProduct;
import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.product.ProductRepository;
import io.wanted.market.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductCoreRepository implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product write(User user, NewProduct newProduct) {
        ProductEntity savedProduct = productJpaRepository.save(
                new ProductEntity(
                        user.id(),
                        newProduct.name(),
                        newProduct.price(),
                        newProduct.quantity()
                )
        );
        return savedProduct.toProduct();
    }
}
