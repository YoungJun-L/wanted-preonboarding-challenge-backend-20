package io.wanted.market.core.api.controller.v1;

import io.wanted.market.RestDocsTest;
import io.wanted.market.core.api.controller.v1.request.RegisterProductRequestDto;
import io.wanted.market.core.domain.order.OrderHistory;
import io.wanted.market.core.domain.order.OrderService;
import io.wanted.market.core.domain.order.OrderStatus;
import io.wanted.market.core.domain.product.*;
import io.wanted.market.core.domain.support.cursor.Cursor;
import io.wanted.market.core.domain.support.cursor.SortType;
import io.wanted.market.core.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static io.wanted.market.RestDocsUtils.requestPreprocessor;
import static io.wanted.market.RestDocsUtils.responsePreprocessor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends RestDocsTest {
    private final ProductService productService = mock(ProductService.class);

    private final OrderService orderService = mock(OrderService.class);

    @Override
    protected Object initController() {
        return new ProductController(productService, orderService);
    }

    @DisplayName("제품 등록 성공")
    @Test
    void registerProduct() throws Exception {
        RegisterProductRequestDto request = new RegisterProductRequestDto(
                "수박",
                BigDecimal.valueOf(1000),
                100L
        );
        Product product = new Product(
                1L,
                1L,
                "수박",
                BigDecimal.valueOf(1000),
                100L,
                100L,
                LocalDateTime.of(2023, 6, 23, 15, 29),
                LocalDateTime.of(2023, 6, 23, 15, 29),
                ProductState.AVAILABLE
        );

        given(productService.createProduct(any(User.class), any(NewProduct.class))).willReturn(product);

        mockMvc.perform(
                        post("/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-post",
                                requestPreprocessor(),
                                responsePreprocessor(),
                                requestFields(
                                        fieldWithPath("productName").type(JsonFieldType.STRING)
                                                .description("제품 이름, 1자 이상 100자 미만"),
                                        fieldWithPath("salePrice").type(JsonFieldType.NUMBER)
                                                .description("판매 가격, 1,000 이상 1억 미만"),
                                        fieldWithPath("quantity").type(JsonFieldType.NUMBER)
                                                .description("판매 수량, 1개 이상 1,000개 미만")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("status"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("data"),
                                        fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                                                .description("등록 제품 id"),
                                        fieldWithPath("error").type(JsonFieldType.NULL)
                                                .ignored()
                                )
                        )
                );
    }

    @DisplayName("유효하지 않은 제품 이름으로 등록 시 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "", // Empty String
            " ", // Only whitespace
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", // 100 characters
    })
    void registerProductWithInvalidName(String productName) throws Exception {
        RegisterProductRequestDto request = new RegisterProductRequestDto(productName, BigDecimal.TEN, 100L);

        given(productService.createProduct(any(User.class), any(NewProduct.class))).willReturn(null);

        mockMvc.perform(
                        post("/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("유효하지 않은 가격으로 등록 시 실패한다.")
    @ParameterizedTest
    @ValueSource(longs = {
            -1,
            0,
            999,
            100_000_000
    })
    void registerProductWithInvalidSalePrice(long salePrice) throws Exception {
        RegisterProductRequestDto request = new RegisterProductRequestDto("product", BigDecimal.valueOf(salePrice), 100L);

        given(productService.createProduct(any(User.class), any(NewProduct.class))).willReturn(null);

        mockMvc.perform(
                        post("/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("유효하지 않은 판매 수량으로 등록 시 실패한다.")
    @ParameterizedTest
    @ValueSource(longs = {
            -1,
            0,
            1_000
    })
    void registerProductWithInvalidQuantity(long quantity) throws Exception {
        RegisterProductRequestDto request = new RegisterProductRequestDto("product", BigDecimal.TEN, quantity);

        given(productService.createProduct(any(User.class), any(NewProduct.class))).willReturn(null);

        mockMvc.perform(
                        post("/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("제품 조회 성공")
    @Test
    void retrieveProducts() throws Exception {
        List<Product> products = List.of(
                new Product(
                        1L,
                        1L,
                        "수박",
                        BigDecimal.TEN,
                        100L,
                        100L,
                        LocalDateTime.of(2024, 6, 23, 15, 29),
                        LocalDateTime.of(2024, 6, 23, 15, 29),
                        ProductState.AVAILABLE
                ),
                new Product(
                        2L,
                        1L,
                        "사과",
                        BigDecimal.TEN,
                        100L,
                        100L,
                        LocalDateTime.of(2024, 6, 25, 22, 53),
                        LocalDateTime.of(2024, 6, 25, 22, 53),
                        ProductState.AVAILABLE
                )
        );

        given(productService.retrieveProducts(any(Cursor.class))).willReturn(products);

        mockMvc.perform(
                        get("/products")
                                .queryParam("cursor", "1")
                                .queryParam("limit", "2")
                                .queryParam("sortKey", ProductSortKey.UPDATED_AT.name())
                                .queryParam("sortType", SortType.DESC.name())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-get",
                                requestPreprocessor(),
                                responsePreprocessor(),
                                queryParameters(
                                        parameterWithName("cursor").description("다음 페이지 호출에 필요한 cursor, UPDATED_AT(timestamp) | PRICE(price)"),
                                        parameterWithName("limit").description("페이지 크기"),
                                        parameterWithName("sortKey").description("UPDATED_AT(수정 날짜 기준) | PRICE(가격 기준)"),
                                        parameterWithName("sortType").description("ASC(오름차순) | DESC(내림차순)")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("status"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("data"),
                                        fieldWithPath("data.content").type(JsonFieldType.ARRAY)
                                                .ignored(),
                                        fieldWithPath("data.content[].productId").type(JsonFieldType.NUMBER)
                                                .description("제품 id"),
                                        fieldWithPath("data.content[].sellerId").type(JsonFieldType.NUMBER)
                                                .description("판매자 id"),
                                        fieldWithPath("data.content[].name").type(JsonFieldType.STRING)
                                                .description("제품 이름"),
                                        fieldWithPath("data.content[].price").type(JsonFieldType.NUMBER)
                                                .description("제품 가격"),
                                        fieldWithPath("data.content[].totalQuantity").type(JsonFieldType.NUMBER)
                                                .description("전체 판매 수량"),
                                        fieldWithPath("data.content[].stockQuantity").type(JsonFieldType.NUMBER)
                                                .description("남은 판매 수량"),
                                        fieldWithPath("data.content[].registeredAt").type(JsonFieldType.STRING)
                                                .description("제품 등록 날짜"),
                                        fieldWithPath("data.content[].updatedAt").type(JsonFieldType.STRING)
                                                .description("제품 수정 날짜"),
                                        fieldWithPath("data.content[].state").type(JsonFieldType.STRING)
                                                .description("판매 상태, SALE(판매중) | RESERVED(예약됨) | OUT_OF_STOCK(재고 없음)"),
                                        fieldWithPath("data.paging").type(JsonFieldType.OBJECT)
                                                .ignored(),
                                        fieldWithPath("data.paging.nextCursor").type(JsonFieldType.NUMBER)
                                                .description("다음 페이지 커서"),
                                        fieldWithPath("data.paging.hasNext").type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 존재 여부"),
                                        fieldWithPath("error").type(JsonFieldType.NULL)
                                                .ignored()
                                )
                        )
                );
    }

    @DisplayName("제품 상세 조회 성공")
    @Test
    void retrieveProductDetails() throws Exception {
        Product product = new Product(
                1L,
                1L,
                "수박",
                BigDecimal.valueOf(20_000),
                100L,
                100L,
                LocalDateTime.of(2024, 6, 23, 15, 29),
                LocalDateTime.of(2024, 6, 23, 15, 29),
                ProductState.AVAILABLE
        );
        List<OrderHistory> orderHistories = List.of(
                new OrderHistory(
                        1L,
                        1L,
                        2L,
                        "buyerA",
                        1L,
                        1L,
                        "수박",
                        BigDecimal.valueOf(25_000),
                        OrderStatus.APPROVED,
                        LocalDateTime.of(2024, 6, 24, 20, 30)
                ),
                new OrderHistory(
                        1L,
                        1L,
                        2L,
                        "buyerA",
                        1L,
                        1L,
                        "수박",
                        BigDecimal.valueOf(20_000),
                        OrderStatus.CANCELED,
                        LocalDateTime.of(2024, 6, 25, 8, 0)
                )
        );

        given(productService.retrieveProduct(anyLong())).willReturn(product);
        given(orderService.retrieveOrderHistories(any(User.class), any(Product.class))).willReturn(orderHistories);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/products/{productId}", product.id())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-details-get",
                                requestPreprocessor(),
                                responsePreprocessor(),
                                pathParameters(
                                        parameterWithName("productId").description("제품 id")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("status"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("data"),
                                        fieldWithPath("data.product").type(JsonFieldType.OBJECT)
                                                .description("제품 상세 정보"),
                                        fieldWithPath("data.product.productId").type(JsonFieldType.NUMBER)
                                                .description("제품 id"),
                                        fieldWithPath("data.product.sellerId").type(JsonFieldType.NUMBER)
                                                .description("판매자 id"),
                                        fieldWithPath("data.product.name").type(JsonFieldType.STRING)
                                                .description("제품 이름"),
                                        fieldWithPath("data.product.price").type(JsonFieldType.NUMBER)
                                                .description("제품 가격"),
                                        fieldWithPath("data.product.totalQuantity").type(JsonFieldType.NUMBER)
                                                .description("전체 판매 수량"),
                                        fieldWithPath("data.product.stockQuantity").type(JsonFieldType.NUMBER)
                                                .description("남은 판매 수량"),
                                        fieldWithPath("data.product.registeredAt").type(JsonFieldType.STRING)
                                                .description("제품 등록 날짜"),
                                        fieldWithPath("data.product.updatedAt").type(JsonFieldType.STRING)
                                                .description("제품 수정 날짜"),
                                        fieldWithPath("data.product.state").type(JsonFieldType.STRING)
                                                .description("판매 상태, SALE(판매중) | RESERVED(예약됨) | OUT_OF_STOCK(재고 없음)"),
                                        fieldWithPath("data.histories").type(JsonFieldType.ARRAY)
                                                .description("제품 거래 내역")
                                                .optional(),
                                        fieldWithPath("data.histories[].orderId").type(JsonFieldType.NUMBER)
                                                .description("주문 id"),
                                        fieldWithPath("data.histories[].buyerId").type(JsonFieldType.NUMBER)
                                                .description("구매자 id"),
                                        fieldWithPath("data.histories[].buyerUsername").type(JsonFieldType.STRING)
                                                .description("구매자 이름"),
                                        fieldWithPath("data.histories[].price").type(JsonFieldType.NUMBER)
                                                .description("주문 당시 가격"),
                                        fieldWithPath("data.histories[].status").type(JsonFieldType.STRING)
                                                .description("주문 상태"),
                                        fieldWithPath("data.histories[].createdAt").type(JsonFieldType.STRING)
                                                .description("주문 날짜"),
                                        fieldWithPath("error").type(JsonFieldType.NULL)
                                                .ignored()
                                )
                        )
                );
    }
}
