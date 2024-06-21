package io.wanted.market.auth.api.security.filter;

import io.wanted.market.auth.api.security.SecurityTest;
import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.domain.auth.AuthStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static io.wanted.market.RestDocsUtils.requestPreprocessor;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JwtAuthenticationTest extends SecurityTest {
    @DisplayName("JWT 인증 성공")
    @Test
    void jwtAuthenticate() throws Exception {
        String accessToken = "accessToken";
        Auth auth = new Auth(1L, "username", "password", AuthStatus.ENABLED);
        given(tokenParser.parseSubject(accessToken)).willReturn("username");
        given(authService.loadUserByUsername("username")).willReturn(auth);

        mockMvc.perform(
                        get("/test")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("authenticate",
                                requestPreprocessor(),
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token").optional()
                                )
                        )
                );
    }

    @DisplayName("JWT 인증 실패")
    @Test
    void jwtAuthenticateFailed() throws Exception {
        String token = "token";
        given(tokenParser.parseSubject(token)).willThrow(new BadTokenException("error"));

        mockMvc.perform(
                        get("/test")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
