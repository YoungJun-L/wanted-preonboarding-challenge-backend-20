package io.wanted.market.core.api.controller.v1.request;

import io.wanted.market.core.domain.product.NewProduct;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record RegisterProductRequestDto(
        @NotBlank
        @Size(min = 1, max = 99)
        String productName,

        @Min(1)
        @Max(99_999_999)
        BigDecimal salePrice,

        @Min(1)
        @Max(999)
        Long quantity
) {
    public NewProduct toNewProduct() {
        return new NewProduct(productName, salePrice, quantity);
    }
}
