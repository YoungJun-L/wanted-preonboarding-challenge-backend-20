package io.wanted.market.auth.api.security.filter;

import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.domain.auth.AuthService;
import io.wanted.market.auth.domain.token.TokenParser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final TokenParser tokenParser;

    private final AuthService authService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String token = (String) authentication.getPrincipal();
        try {
            String username = tokenParser.parseSubject(token);
            return createSuccessAuthentication(username);
        } catch (Exception ex) {
            throw new BadTokenException(ex.getMessage(), ex.getCause());
        }
    }

    private Authentication createSuccessAuthentication(String username) {
        Auth auth = authService.loadUserByUsername(username);
        return JwtAuthenticationToken.authenticated(auth);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BearerTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
