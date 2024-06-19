package io.wanted.market.auth.api.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wanted.market.auth.api.security.response.LoginResponseDto;
import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.domain.token.TokenPair;
import io.wanted.market.auth.domain.token.TokenService;
import io.wanted.market.core.api.support.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class IssueJwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenService tokenService;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Auth auth = (Auth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TokenPair tokenPair = tokenService.issue(auth);
        ApiResponse<?> apiResponse = ApiResponse.success(LoginResponseDto.from(tokenPair));
        write(response, apiResponse);
    }

    private void write(HttpServletResponse response, ApiResponse<?> apiResponse) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
