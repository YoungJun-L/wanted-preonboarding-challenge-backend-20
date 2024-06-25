package io.wanted.market.auth.api.controller.v1;

import io.wanted.market.RestDocsTest;
import io.wanted.market.auth.api.controller.v1.request.ReissueTokenRequestDto;
import io.wanted.market.auth.domain.token.TokenPair;
import io.wanted.market.auth.domain.token.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.nio.charset.StandardCharsets;

import static io.wanted.market.RestDocsUtils.requestPreprocessor;
import static io.wanted.market.RestDocsUtils.responsePreprocessor;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TokenControllerTest extends RestDocsTest {
    private static final String ACCESS_TOKEN = "${ACCESS_TOKEN}";

    private static final Long ACCESS_TOKEN_EXPIRES_IN = 1L;

    private static final String REFRESH_TOKEN = "${REFRESH_TOKEN}";

    private static final Long REFRESH_TOKEN_EXPIRES_IN = 1L;

    private final TokenService tokenService = mock(TokenService.class);

    @Override
    protected Object initController() {
        return new TokenController(tokenService);
    }

    @DisplayName("재발급 성공")
    @Test
    void reissue() throws Exception {
        ReissueTokenRequestDto request = new ReissueTokenRequestDto("${REFRESH_TOKEN}");

        given(tokenService.reissue(anyString())).willReturn(buildTokenPair());

        mockMvc.perform(
                        post("/auth/token")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("token",
                                requestPreprocessor(),
                                responsePreprocessor(),
                                requestFields(
                                        fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                                .description("refreshToken")),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("status"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("data"),
                                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                                .description("accessToken"),
                                        fieldWithPath("data.accessTokenExpiresIn").type(JsonFieldType.NUMBER)
                                                .description("accessToken 만료 시간, UNIX 타임스탬프(Timestamp)"),
                                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                                                .description("refreshToken"),
                                        fieldWithPath("data.refreshTokenExpiresIn").type(JsonFieldType.NUMBER)
                                                .description("refreshToken 만료 시간, UNIX 타임스탬프(Timestamp)"),
                                        fieldWithPath("error").type(JsonFieldType.NULL)
                                                .description("error")
                                                .ignored()
                                )
                        )
                );
    }

    @DisplayName("유효하지 않은 refresh token 으로 재발급 시 실패한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "", // Empty String
            " ", // Only whitespace
    })
    void reissueWithInvalidRefreshToken(String invalidRefreshToken) throws Exception {
        ReissueTokenRequestDto request = new ReissueTokenRequestDto(invalidRefreshToken);

        given(tokenService.reissue(anyString())).willReturn(null);

        mockMvc.perform(
                        post("/auth/token")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private TokenPair buildTokenPair() {
        return new TokenPair(
                1L,
                ACCESS_TOKEN,
                ACCESS_TOKEN_EXPIRES_IN,
                REFRESH_TOKEN,
                REFRESH_TOKEN_EXPIRES_IN
        );
    }
}
