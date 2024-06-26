package io.wanted.market.core.domain.product;

import java.util.List;
import java.util.function.Function;

public enum ProductSortKey {
    UPDATED_AT(Product::updatedAtTimeStamp),
    PRICE(product -> product.price().longValue());

    private final Function<Product, Long> function;

    ProductSortKey(Function<Product, Long> function) {
        this.function = function;
    }

    public Long generateNextCursor(List<Product> products, Long limit) {
        if (products.size() < limit) {
            return null;
        }
        return function.apply(products.get(products.size() - 1));
    }
}
