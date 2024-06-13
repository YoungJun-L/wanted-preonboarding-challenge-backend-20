package io.wanted.market.auth.api.controller.v1;

import io.wanted.market.RestDocsTest;
import io.wanted.market.auth.api.controller.v1.request.ReissueRequestDto;
import io.wanted.market.auth.api.controller.v1.request.UserRegisterRequestDto;
import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.domain.auth.AuthService;
import io.wanted.market.auth.domain.token.TokenPair;
import io.wanted.market.auth.domain.token.TokenService;
import io.wanted.market.auth.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static io.wanted.market.RestDocsUtils.requestPreprocessor;
import static io.wanted.market.RestDocsUtils.responsePreprocessor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
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

    private final AuthService authService = mock(AuthService.class);

    private final TokenService tokenService = mock(TokenService.class);

    @Override
    protected Object initController() {
        return new AuthController(authService, tokenService);
    }

//    @DisplayName("로그인 성공")
//    @Test
//    void login() throws Exception {
//        LoginRequestDto request = new LoginRequestDto(VALID_USERNAME, VALID_PASSWORD);
//
//        given(tokenService.issue(anyString())).willReturn(buildTokenPair());
//
//        mockMvc.perform(
//                        post("/auth/login")
//                                .content(objectMapper.writeValueAsString(request))
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("login",
//                                requestPreprocessor(),
//                                responsePreprocessor(),
//                                requestFields(
//                                        fieldWithPath("username").type(JsonFieldType.STRING)
//                                                .description("username"),
//                                        fieldWithPath("password").type(JsonFieldType.STRING)
//                                                .description("password")),
//                                responseFields(
//                                        fieldWithPath("status").type(JsonFieldType.STRING)
//                                                .description("status"),
//                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
//                                                .description("data"),
//                                        fieldWithPath("data.userId").type(JsonFieldType.STRING)
//                                                .description("발급 userId"),
//                                        fieldWithPath("data.tokens").type(JsonFieldType.OBJECT)
//                                                .description("tokens"),
//                                        fieldWithPath("data.tokens.accessToken").type(JsonFieldType.STRING)
//                                                .description("accessToken"),
//                                        fieldWithPath("data.tokens.accessTokenExpiresIn").type(JsonFieldType.NUMBER)
//                                                .description("accessToken 만료 시간"),
//                                        fieldWithPath("data.tokens.refreshToken").type(JsonFieldType.STRING)
//                                                .description("refreshToken"),
//                                        fieldWithPath("data.tokens.refreshTokenExpiresIn").type(JsonFieldType.NUMBER)
//                                                .description("refreshToken 만료 시간"),
//                                        fieldWithPath("error").type(JsonFieldType.NULL)
//                                                .description("error")
//                                )
//                        )
//                );
//    }
//
//    @DisplayName("유효하지 않은 아이디로 로그인 시 실패한다.")
//    @ParameterizedTest
//    @ValueSource(strings = {
//            "", // Empty String
//            "abcd123", // Less than 8 characters
//            "0123456789abcdefghijabcdefghijabcdefghijabcdefghij", // 50 characters
//            "abcdefgh", // Only characters
//            "01234567", // Only digits
//            "abcdef 123", // Contain whitespace
//    })
//    void loginWithInvalidUsername(String invalidUsername) throws Exception {
//        LoginRequestDto request = new LoginRequestDto(invalidUsername, VALID_PASSWORD);
//
//        given(tokenService.issue(anyString())).willReturn(null);
//
//        mockMvc.perform(
//                        post("/auth/login")
//                                .content(objectMapper.writeValueAsString(request))
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }
//
//    @DisplayName("유효하지 않은 비밀번호로 로그인 시 실패한다.")
//    @ParameterizedTest
//    @ValueSource(strings = {
//            "", // Empty String
//            "abcdef123", // Less than 10 characters
//            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij", // 50 characters
//            "abcdefgh", // Only characters
//            "01234567", // Only digits
//            "!@#$%^&*", // Only special characters
//            "abcdef 123 !", // Contain whitespace
//    })
//    void loginWithInvalidPassword(String invalidPassword) throws Exception {
//        LoginRequestDto request = new LoginRequestDto(VALID_USERNAME, invalidPassword);
//
//        given(tokenService.issue(anyString())).willReturn(null);
//
//        mockMvc.perform(
//                        post("/auth/login")
//                                .content(objectMapper.writeValueAsString(request))
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }

    @DisplayName("재발급 성공")
    @Test
    void reissue() throws Exception {
        ReissueRequestDto request = new ReissueRequestDto("refreshToken");

        given(tokenService.reissue(anyString())).willReturn(buildTokenPair());

        mockMvc.perform(
                        post("/auth/reissue")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("reissue",
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
                                                .description("accessToken 만료 시간"),
                                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                                                .description("refreshToken"),
                                        fieldWithPath("data.refreshTokenExpiresIn").type(JsonFieldType.NUMBER)
                                                .description("refreshToken 만료 시간"),
                                        fieldWithPath("error").type(JsonFieldType.NULL)
                                                .description("error")
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
        ReissueRequestDto request = new ReissueRequestDto(invalidRefreshToken);

        given(tokenService.reissue(anyString())).willReturn(null);

        mockMvc.perform(
                        post("/auth/reissue")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
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

    @DisplayName("회원가입 성공")
    @Test
    void register() throws Exception {
        UserRegisterRequestDto request = new UserRegisterRequestDto(VALID_USERNAME, VALID_PASSWORD);

        doNothing().when(authService).register(any(User.class), any(Auth.class));

        mockMvc.perform(
                        post("/auth/register")
                                .param("username", "username")
                                .param("password", "password")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("register",
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
                                        fieldWithPath("data").type(JsonFieldType.NULL)
                                                .description("data"),
                                        fieldWithPath("error").type(JsonFieldType.NULL)
                                                .description("error")
                                )
                        )
                );
    }

    @DisplayName("유효하지 않은 아이디로 회원가입 시 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "", // Empty String
            "abcd123", // Less than 8 characters
            "0123456789abcdefghijabcdefghijabcdefghijabcdefghij", // 50 characters
            "abcdefgh", // Only characters
            "01234567", // Only digits
            "abcdef 123", // Contain whitespace
    })
    void registerWithInvalidUsername(String invalidUsername) throws Exception {
        UserRegisterRequestDto request = new UserRegisterRequestDto(invalidUsername, VALID_PASSWORD);

        doNothing().when(authService).register(any(User.class), any(Auth.class));

        mockMvc.perform(
                        post("/auth/register")
                                .param("username", "username")
                                .param("password", "password")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("유효하지 않은 비밀번호로 회원가입 시 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "", // Empty String
            "abcdef123", // Less than 10 characters
            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij", // 50 characters
            "abcdefgh", // Only characters
            "01234567", // Only digits
            "!@#$%^&*", // Only special characters
            "abcdef 123 !", // Contain whitespace
    })
    void registerWithInvalidPassword(String invalidPassword) throws Exception {
        UserRegisterRequestDto request = new UserRegisterRequestDto(VALID_USERNAME, invalidPassword);

        doNothing().when(authService).register(any(User.class), any(Auth.class));

        mockMvc.perform(
                        post("/auth/register")
                                .param("username", "username")
                                .param("password", "password")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
