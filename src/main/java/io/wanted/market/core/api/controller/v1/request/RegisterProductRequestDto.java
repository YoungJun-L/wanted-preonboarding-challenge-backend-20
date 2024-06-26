package io.wanted.market.core.api.controller.v1.request;

import io.wanted.market.core.domain.product.NewProduct;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record RegisterProductRequestDto(
        @NotBlank
        @Size(min = 1, max = 99)
        String productName,

        @NotNull
        @Min(1_000)
        @Digits(integer = 8, fraction = 0)
        BigDecimal salePrice,

        @NotNull
        @Positive
        @Max(999)
        Long quantity
) {
    public NewProduct toNewProduct() {
        return new NewProduct(productName, salePrice, quantity);
    }
}
