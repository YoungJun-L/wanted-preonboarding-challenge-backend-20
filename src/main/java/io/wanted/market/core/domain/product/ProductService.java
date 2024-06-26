package io.wanted.market.core.domain.product;

import io.wanted.market.core.domain.support.cursor.Cursor;
import io.wanted.market.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductReader productReader;

    private final ProductWriter productWriter;

    public Product createProduct(User user, NewProduct newProduct) {
        return productWriter.write(user, newProduct);
    }

    public List<Product> retrieveProducts(Cursor cursor) {
        return productReader.read(cursor);
    }
}
