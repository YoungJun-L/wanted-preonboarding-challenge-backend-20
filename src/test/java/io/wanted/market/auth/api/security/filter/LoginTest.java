package io.wanted.market.auth.api.security.filter;

import io.wanted.market.auth.api.security.SecurityTest;
import io.wanted.market.auth.api.security.request.LoginRequestDto;
import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.domain.auth.AuthStatus;
import io.wanted.market.auth.domain.token.TokenPair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.nio.charset.StandardCharsets;

import static io.wanted.market.RestDocsUtils.requestPreprocessor;
import static io.wanted.market.RestDocsUtils.responsePreprocessor;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginTest extends SecurityTest {
    @DisplayName("로그인 성공")
    @Test
    void login() throws Exception {
        LoginRequestDto request = new LoginRequestDto("${USERNAME}", "${PASSWORD}");
        Auth auth = new Auth(1L, "${USERNAME}", "${PASSWORD}", AuthStatus.ENABLED);
        given(authService.loadUserByUsername("${USERNAME}")).willReturn(auth);
        given(tokenService.issue(auth))
                .willReturn(new TokenPair(auth.id(), "${ACCESS_TOKEN}", 1L, "${REFRESH_TOKEN}", 1L));

        mockMvc.perform(
                        post("/auth/login")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
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
                                                .description("password")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("status"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("data"),
                                        fieldWithPath("data.userId").type(JsonFieldType.NUMBER)
                                                .description("발급 userId"),
                                        fieldWithPath("data.tokens").type(JsonFieldType.OBJECT)
                                                .description("tokens"),
                                        fieldWithPath("data.tokens.accessToken").type(JsonFieldType.STRING)
                                                .description("accessToken"),
                                        fieldWithPath("data.tokens.accessTokenExpiresIn").type(JsonFieldType.NUMBER)
                                                .description("accessToken 만료 시간, UNIX 타임스탬프(Timestamp)"),
                                        fieldWithPath("data.tokens.refreshToken").type(JsonFieldType.STRING)
                                                .description("refreshToken"),
                                        fieldWithPath("data.tokens.refreshTokenExpiresIn").type(JsonFieldType.NUMBER)
                                                .description("refreshToken 만료 시간, UNIX 타임스탬프(Timestamp)"),
                                        fieldWithPath("error").type(JsonFieldType.NULL)
                                                .description("error")
                                )
                        )
                );
    }

    @DisplayName("가입하지 않은 회원이 로그인 시 실패한다.")
    @Test
    void loginFailedWithNotRegisteredUser() throws Exception {
        LoginRequestDto request = new LoginRequestDto("username", "password");
        given(authService.loadUserByUsername("username")).willThrow(new UsernameNotFoundException("error"));

        mockMvc.perform(
                        post("/auth/login")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("서비스 이용이 불가능한 회원이 로그인 시 실패한다.")
    @Test
    void loginFailedWithLockedUser() throws Exception {
        LoginRequestDto request = new LoginRequestDto("username", "password");
        Auth auth = new Auth(1L, "username", "password", AuthStatus.LOCKED);
        given(authService.loadUserByUsername("username")).willReturn(auth);

        mockMvc.perform(
                        post("/auth/login")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
