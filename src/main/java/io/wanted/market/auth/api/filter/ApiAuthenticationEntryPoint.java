package io.wanted.market.auth.api.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wanted.market.auth.api.support.error.AuthErrorType;
import io.wanted.market.auth.api.support.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        AuthErrorType authErrorType = resolve(ex);
        log(ex, authErrorType);
        write(response, authErrorType);
    }

    private AuthErrorType resolve(AuthenticationException ex) {
        AuthErrorType authErrorType;
        if (ex instanceof BadCredentialsException) {
            authErrorType = AuthErrorType.AUTH_BAD_CREDENTIALS_ERROR;
        } else {
            authErrorType = AuthErrorType.UNAUTHORIZED_ERROR;
        }
        return authErrorType;
    }

    private void log(AuthenticationException ex, AuthErrorType authErrorType) {
        switch (authErrorType.getLogLevel()) {
            case ERROR -> log.error("AuthException : {}", ex.getMessage(), ex);
            case WARN -> log.warn("AuthException : {}", ex.getMessage(), ex);
            default -> log.info("AuthException : {}", ex.getMessage(), ex);
        }
    }

    private void write(HttpServletResponse response, AuthErrorType authErrorType) throws IOException {
        response.setStatus(authErrorType.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(AuthResponse.error(authErrorType)));
    }
}
