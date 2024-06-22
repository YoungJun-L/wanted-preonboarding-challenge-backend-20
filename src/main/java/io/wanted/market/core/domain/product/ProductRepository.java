package io.wanted.market.core.domain.product;

import io.wanted.market.core.domain.user.User;

public interface ProductRepository {
    Product write(User user, NewProduct newProduct);
}
