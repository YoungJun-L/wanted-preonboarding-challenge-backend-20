package io.wanted.market.core.api.controller.v1;

import io.wanted.market.core.api.controller.v1.request.RegisterProductRequestDto;
import io.wanted.market.core.api.controller.v1.response.ProductDetailsResponseDto;
import io.wanted.market.core.api.controller.v1.response.ProductResponseDto;
import io.wanted.market.core.api.controller.v1.response.RegisterProductResponseDto;
import io.wanted.market.core.api.support.response.ApiResponse;
import io.wanted.market.core.api.support.response.SliceResult;
import io.wanted.market.core.domain.order.OrderHistory;
import io.wanted.market.core.domain.order.OrderService;
import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.product.ProductService;
import io.wanted.market.core.domain.product.ProductSortKey;
import io.wanted.market.core.domain.support.cursor.Cursor;
import io.wanted.market.core.domain.support.cursor.SortType;
import io.wanted.market.core.domain.user.AnyUser;
import io.wanted.market.core.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;

    private final OrderService orderService;

    @PostMapping("/products")
    public ApiResponse<RegisterProductResponseDto> registerProduct(
            User user,
            @RequestBody @Valid RegisterProductRequestDto request
    ) {
        Product product = productService.createProduct(user, request.toNewProduct());
        return ApiResponse.success(RegisterProductResponseDto.from(product));
    }

    @GetMapping("/products")
    public ApiResponse<SliceResult<List<ProductResponseDto>>> retrieveProducts(
            @RequestParam Long cursor,
            @RequestParam Long limit,
            @RequestParam ProductSortKey sortKey,
            @RequestParam SortType sortType
    ) {
        List<Product> products = productService.retrieveProducts(Cursor.of(cursor, limit, sortKey, sortType));
        Long nextCursor = sortKey.generateNextCursor(products, limit);
        return ApiResponse.success(SliceResult.of(ProductResponseDto.list(products), nextCursor));
    }

    @GetMapping("/products/{productId}")
    public ApiResponse<ProductDetailsResponseDto> retrieveProductDetails(
            AnyUser anyUser,
            @PathVariable Long productId
    ) {
        Product product = productService.retrieveProduct(productId);
        List<OrderHistory> orderHistories = orderService.retrieveOrderHistories(anyUser.toUser(), product);
        return ApiResponse.success(ProductDetailsResponseDto.of(product, orderHistories));
    }
}
