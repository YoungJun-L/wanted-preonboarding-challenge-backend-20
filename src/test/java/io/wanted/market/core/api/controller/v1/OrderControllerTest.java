package io.wanted.market.core.api.controller.v1;

import io.wanted.market.RestDocsTest;
import io.wanted.market.core.api.controller.v1.request.OrderRequestDto;
import io.wanted.market.core.domain.order.NewOrder;
import io.wanted.market.core.domain.order.Order;
import io.wanted.market.core.domain.order.OrderService;
import io.wanted.market.core.domain.order.OrderStatus;
import io.wanted.market.core.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

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

class OrderControllerTest extends RestDocsTest {
    private final OrderService orderService = mock(OrderService.class);

    @Override
    protected Object initController() {
        return new OrderController(orderService);
    }

    @DisplayName("주문 성공")
    @Test
    void order() throws Exception {
        OrderRequestDto request = new OrderRequestDto(1L);

        Order order = new Order(
                1L,
                1L,
                1L,
                OrderStatus.NEW,
                LocalDateTime.of(2024, 7, 2, 16, 12),
                LocalDateTime.of(2024, 7, 2, 16, 12)
        );

        given(orderService.createOrder(any(User.class), any(NewOrder.class))).willReturn(order);

        mockMvc.perform(
                        post("/orders")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("order-post",
                                requestPreprocessor(),
                                responsePreprocessor(),
                                requestFields(
                                        fieldWithPath("productId").description("주문할 제품 id")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("status"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("data"),
                                        fieldWithPath("data.orderId").type(JsonFieldType.NUMBER)
                                                .description("주문 id"),
                                        fieldWithPath("error").type(JsonFieldType.NULL)
                                                .ignored()
                                )
                        )
                );
    }
}
