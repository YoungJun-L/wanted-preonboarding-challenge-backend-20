package io.wanted.market.auth.api.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthDetailsExchangeFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        AuthDetails authDetails = AuthDetails.from((JwtAuthenticationToken) authentication);
        String token = objectMapper.writeValueAsString(authDetails);
        AuthRequest authRequest = new AuthRequest(request, token);
        SecurityContextHolder.clearContext();
        filterChain.doFilter(authRequest, response);
    }

    private static class AuthRequest extends HttpServletRequestWrapper {
        private static final String AUTH_COOKIE_NAME = "user";

        private final Cookie[] cookies;

        private AuthRequest(HttpServletRequest request, String token) {
            super(request);
            Cookie[] original = Optional.ofNullable(request.getCookies()).orElse(new Cookie[]{});
            cookies = Arrays.copyOf(original, original.length + 1);
            cookies[original.length] = new Cookie(AUTH_COOKIE_NAME, token);
        }

        @Override
        public Cookie[] getCookies() {
            return cookies;
        }
    }

    private record AuthDetails(Long userId, Map<String, String> details) {
        private static AuthDetails from(JwtAuthenticationToken jwtAuthenticationToken) {
            return new AuthDetails(jwtAuthenticationToken.getUserId(), jwtAuthenticationToken.getDetails());
        }
    }
}
