package io.wanted.market.core.domain.product;

import io.wanted.market.core.domain.support.cursor.Cursor;
import io.wanted.market.core.domain.user.User;

import java.util.List;

public interface ProductRepository {
    Product write(User user, NewProduct newProduct);

    List<Product> read(Cursor cursor);
}
