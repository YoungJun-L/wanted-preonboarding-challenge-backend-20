package io.wanted.market.core.domain.product;

import io.wanted.market.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductWriter {
    private final ProductRepository productRepository;

    public Product write(User user, NewProduct newProduct) {
        return productRepository.write(user, newProduct);
    }
}
