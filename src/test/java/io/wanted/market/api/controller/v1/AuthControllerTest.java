package io.wanted.market.api.controller.v1;

import io.wanted.market.RestDocsTest;
import io.wanted.market.api.controller.v1.request.LoginRequestDto;
import io.wanted.market.domain.token.TokenPair;
import io.wanted.market.domain.token.TokenService;
import io.wanted.market.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

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

class AuthControllerTest extends RestDocsTest {
    private static final String USER_ID = "userId";

    private static final String ACCESS_TOKEN = "accessToken";

    private static final Long ACCESS_TOKEN_EXPIRES_IN = 1L;

    private static final String REFRESH_TOKEN = "refreshToken";

    private static final Long REFRESH_TOKEN_EXPIRES_IN = 1L;

    private static final String VALID_USERNAME = "username123";

    private static final String VALID_PASSWORD = "password123!";

    private final UserService userService = mock(UserService.class);

    private final TokenService tokenService = mock(TokenService.class);

    @Override
    protected Object initController() {
        return new AuthController(userService, tokenService);
    }

    @DisplayName("로그인 성공")
    @Test
    void login() throws Exception {
        LoginRequestDto request = new LoginRequestDto(VALID_USERNAME, VALID_PASSWORD);

        given(tokenService.issue(anyString())).willReturn(buildTokenPair());

        mockMvc.perform(
                        post("/auth/login")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("login",
                                requestPreprocessor(),
                                responsePreprocessor(),
                                requestFields(
                                        fieldWithPath("username").type(JsonFieldType.STRING)
                                                .description("username"),
                                        fieldWithPath("password").type(JsonFieldType.STRING)
                                                .description("password")),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("status"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("data"),
                                        fieldWithPath("data.userId").type(JsonFieldType.STRING)
                                                .description("발급 userId"),
                                        fieldWithPath("data.tokens").type(JsonFieldType.OBJECT)
                                                .description("tokens"),
                                        fieldWithPath("data.tokens.accessToken").type(JsonFieldType.STRING)
                                                .description("accessToken"),
                                        fieldWithPath("data.tokens.accessTokenExpiresIn").type(JsonFieldType.NUMBER)
                                                .description("accessToken 만료 시간"),
                                        fieldWithPath("data.tokens.refreshToken").type(JsonFieldType.STRING)
                                                .description("refreshToken"),
                                        fieldWithPath("data.tokens.refreshTokenExpiresIn").type(JsonFieldType.NUMBER)
                                                .description("refreshToken 만료 시간"),
                                        fieldWithPath("error").type(JsonFieldType.NULL)
                                                .description("error")
                                )
                        )
                );
    }

    private TokenPair buildTokenPair() {
        return new TokenPair(
                USER_ID,
                ACCESS_TOKEN,
                ACCESS_TOKEN_EXPIRES_IN,
                REFRESH_TOKEN,
                REFRESH_TOKEN_EXPIRES_IN
        );
    }
}
