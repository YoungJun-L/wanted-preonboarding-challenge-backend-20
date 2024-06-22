package io.wanted.market.core.api.controller.v1;

import io.wanted.market.RestDocsTest;
import io.wanted.market.core.api.controller.v1.request.RegisterProductRequestDto;
import io.wanted.market.core.domain.product.NewProduct;
import io.wanted.market.core.domain.product.Product;
import io.wanted.market.core.domain.product.ProductService;
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

import static io.wanted.market.RestDocsUtils.requestPreprocessor;
import static io.wanted.market.RestDocsUtils.responsePreprocessor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
    void register() throws Exception {
        RegisterProductRequestDto request = new RegisterProductRequestDto(
                "수박",
                BigDecimal.TEN,
                100L
        );
        Product product = new Product(1L, 1L, "productName", BigDecimal.TEN, 100L, LocalDateTime.now());

        given(productService.register(any(User.class), any(NewProduct.class))).willReturn(product);

        mockMvc.perform(
                        post("/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product",
                                requestPreprocessor(),
                                responsePreprocessor(),
                                requestFields(
                                        fieldWithPath("productName").type(JsonFieldType.STRING)
                                                .description("제품 이름, 1자 이상 100자 미만"),
                                        fieldWithPath("salePrice").type(JsonFieldType.NUMBER)
                                                .description("판매 가격, 1 이상 1억 미만"),
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
    void registerWithInvalidName(String productName) throws Exception {
        RegisterProductRequestDto request = new RegisterProductRequestDto(productName, BigDecimal.TEN, 100L);

        given(productService.register(any(User.class), any(NewProduct.class))).willReturn(null);

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
            100_000_000
    })
    void registerWithInvalidSalePrice(long salePrice) throws Exception {
        RegisterProductRequestDto request = new RegisterProductRequestDto("product", BigDecimal.valueOf(salePrice), 100L);

        given(productService.register(any(User.class), any(NewProduct.class))).willReturn(null);

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
    void registerWithInvalidQuantity(long quantity) throws Exception {
        RegisterProductRequestDto request = new RegisterProductRequestDto("product", BigDecimal.TEN, quantity);

        given(productService.register(any(User.class), any(NewProduct.class))).willReturn(null);

        mockMvc.perform(
                        post("/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
