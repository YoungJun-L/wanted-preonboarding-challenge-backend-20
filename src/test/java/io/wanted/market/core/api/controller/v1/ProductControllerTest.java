package io.wanted.market.core.api.controller.v1;

import io.wanted.market.RestDocsTest;
import io.wanted.market.core.api.controller.v1.request.RegisterProductRequestDto;
import io.wanted.market.core.domain.product.*;
import io.wanted.market.core.domain.support.cursor.Cursor;
import io.wanted.market.core.domain.support.cursor.SortType;
import io.wanted.market.core.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static io.wanted.market.RestDocsUtils.requestPreprocessor;
import static io.wanted.market.RestDocsUtils.responsePreprocessor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends RestDocsTest {
    private final ProductService productService = mock(ProductService.class);

    @Override
    protected Object initController() {
        return new ProductController(productService);
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
                ProductStatus.SALE
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
                        ProductStatus.SALE
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
                        ProductStatus.SALE
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
                                        fieldWithPath("data.content[].status").type(JsonFieldType.STRING)
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
}
