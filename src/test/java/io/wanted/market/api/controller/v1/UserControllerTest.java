package io.wanted.market.api.controller.v1;

import io.wanted.market.RestDocsTest;
import io.wanted.market.api.controller.v1.request.RegisterRequestDto;
import io.wanted.market.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static io.wanted.market.RestDocsUtils.requestPreprocessor;
import static io.wanted.market.RestDocsUtils.responsePreprocessor;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends RestDocsTest {
    private static final String VALID_USERNAME = "username123";

    private static final String VALID_PASSWORD = "password123!";

    private final UserService userService = mock(UserService.class);

    @Override
    protected Object initController() {
        return new UserController(userService);
    }

    @DisplayName("회원가입 성공")
    @Test
    void register() throws Exception {
        RegisterRequestDto request = new RegisterRequestDto(VALID_USERNAME, VALID_PASSWORD);

        doNothing().when(userService).register(anyString(), anyString());

        mockMvc.perform(
                        post("/auth/register")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
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
    void registerWithInvalidUsername(String inValidUsername) throws Exception {
        RegisterRequestDto request = new RegisterRequestDto(inValidUsername, VALID_PASSWORD);

        doNothing().when(userService).register(anyString(), anyString());

        mockMvc.perform(
                        post("/auth/register")
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
    void registerWithInvalidPassword(String inValidPassword) throws Exception {
        RegisterRequestDto request = new RegisterRequestDto(VALID_USERNAME, inValidPassword);

        doNothing().when(userService).register(anyString(), anyString());

        mockMvc.perform(
                        post("/auth/register")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
