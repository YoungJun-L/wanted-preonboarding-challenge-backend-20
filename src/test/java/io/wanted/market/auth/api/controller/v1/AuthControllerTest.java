package io.wanted.market.auth.api.controller.v1;

import io.wanted.market.RestDocsTest;
import io.wanted.market.auth.api.controller.v1.request.RegisterAuthRequestDto;
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

import java.nio.charset.StandardCharsets;

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
    private static final String VALID_USERNAME = "username123";

    private static final String VALID_PASSWORD = "password123!";

    private final AuthService authService = mock(AuthService.class);

    @Override
    protected Object initController() {
        return new AuthController(authService);
    }

    @DisplayName("회원가입 성공")
    @Test
    void register() throws Exception {
        RegisterAuthRequestDto request = new RegisterAuthRequestDto(VALID_USERNAME, VALID_PASSWORD);

        given(authService.register(any(NewAuth.class)))
                .willReturn(new Auth(1L, "username", "password", AuthStatus.ENABLED));

        mockMvc.perform(
                        post("/auth/register")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("register",
                                requestPreprocessor(),
                                responsePreprocessor(),
                                requestFields(
                                        fieldWithPath("username").type(JsonFieldType.STRING)
                                                .description("username, 최소 8자 이상 최대 50자 미만의 1개 이상 영문자, 1개 이상 숫자 조합"),
                                        fieldWithPath("password").type(JsonFieldType.STRING)
                                                .description("password, 최소 10자 이상 최대 50자 미만의 1개 이상 영문자, 1개 이상 특수문자, 1개 이상 숫자 조합")),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("status"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("data"),
                                        fieldWithPath("data.userId").type(JsonFieldType.NUMBER)
                                                .description("userId"),
                                        fieldWithPath("error").type(JsonFieldType.NULL)
                                                .description("error")
                                                .ignored()
                                )
                        )
                );
    }

    @DisplayName("유효하지 않은 아이디로 회원가입 시 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "", // Empty String
            " ", // Only whitespace
            "abcd123", // Less than 8 characters
            "0123456789abcdefghijabcdefghijabcdefghijabcdefghij", // 50 characters
            "abcdefgh", // Only characters
            "01234567", // Only digits
            "abcdef 123", // Contain whitespace
    })
    void registerWithInvalidUsername(String invalidUsername) throws Exception {
        RegisterAuthRequestDto request = new RegisterAuthRequestDto(invalidUsername, VALID_PASSWORD);

        given(authService.register(any(NewAuth.class))).willReturn(null);

        mockMvc.perform(
                        post("/auth/register")
                                .param("username", "username")
                                .param("password", "password")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("유효하지 않은 비밀번호로 회원가입 시 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "", // Empty String
            " ", // Only whitespace
            "abcdef123", // Less than 10 characters
            "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij", // 50 characters
            "abcdefgh", // Only characters
            "01234567", // Only digits
            "!@#$%^&*", // Only special characters
            "abcdef 123 !", // Contain whitespace
    })
    void registerWithInvalidPassword(String invalidPassword) throws Exception {
        RegisterAuthRequestDto request = new RegisterAuthRequestDto(VALID_USERNAME, invalidPassword);

        given(authService.register(any(NewAuth.class))).willReturn(null);

        mockMvc.perform(
                        post("/auth/register")
                                .param("username", "username")
                                .param("password", "password")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
