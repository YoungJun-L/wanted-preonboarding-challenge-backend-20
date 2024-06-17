package io.wanted.market.auth.api.controller.v1;

import io.wanted.market.RestDocsTest;
import io.wanted.market.auth.api.controller.v1.request.RegisterRequestDto;
import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.domain.auth.AuthService;
import io.wanted.market.auth.domain.auth.AuthStatus;
import io.wanted.market.auth.domain.auth.NewAuth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

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

class AuthControllerTest extends RestDocsTest {
    private static final String USER_ID = "userId";

    private static final String VALID_USERNAME = "username123";

    private static final String VALID_PASSWORD = "password123!";

    private final AuthService authService = mock(AuthService.class);

    @Override
    protected Object initController() {
        return new AuthController(authService);
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
    @DisplayName("회원가입 성공")
    @Test
    void register() throws Exception {
        RegisterRequestDto request = new RegisterRequestDto(VALID_USERNAME, VALID_PASSWORD);

        given(authService.register(any(NewAuth.class))).willReturn(new Auth(1L, "username", "password", AuthStatus.ENABLED));

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
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("data"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                                .description("id"),
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
        RegisterRequestDto request = new RegisterRequestDto(invalidUsername, VALID_PASSWORD);

        given(authService.register(any(NewAuth.class))).willReturn(null);

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
        RegisterRequestDto request = new RegisterRequestDto(VALID_USERNAME, invalidPassword);

        given(authService.register(any(NewAuth.class))).willReturn(null);

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
