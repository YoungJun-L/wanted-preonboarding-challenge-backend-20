package io.wanted.market.core.domain.product;

import io.wanted.market.core.domain.support.cursor.Cursor;
import io.wanted.market.core.domain.support.error.CoreErrorType;
import io.wanted.market.core.domain.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductReader {
    private final ProductRepository productRepository;

    public List<Product> read(Cursor cursor) {
        return productRepository.read(cursor);
    }

    public Product read(Long productId) {
        return productRepository.read(productId)
                .orElseThrow(() -> new CoreException(CoreErrorType.PRODUCT_NOT_FOUND_ERROR));
    }
}
