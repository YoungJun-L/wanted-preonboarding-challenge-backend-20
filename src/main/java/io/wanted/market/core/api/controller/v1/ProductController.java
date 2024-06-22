package io.wanted.market.core.api.controller.v1;

import io.wanted.market.core.api.controller.v1.request.RegisterProductRequestDto;
import io.wanted.market.core.api.controller.v1.response.RegisterProductResponseDto;
import io.wanted.market.core.api.support.response.ApiResponse;
import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.product.ProductService;
import io.wanted.market.core.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;

    @PostMapping("/products")
    public ApiResponse<RegisterProductResponseDto> register(
            User user,
            @RequestBody @Valid RegisterProductRequestDto request) {
        Product product = productService.register(user, request.toNewProduct());
        return ApiResponse.success(RegisterProductResponseDto.from(product));
    }
}
