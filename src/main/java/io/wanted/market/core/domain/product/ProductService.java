package io.wanted.market.core.domain.product;

import io.wanted.market.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductWriter productWriter;

    public Product register(User user, NewProduct newProduct) {
        return productWriter.write(user, newProduct);
    }
}
