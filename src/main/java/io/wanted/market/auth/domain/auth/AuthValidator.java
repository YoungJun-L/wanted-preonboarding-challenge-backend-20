package io.wanted.market.auth.domain.auth;

import io.wanted.market.auth.api.support.error.AuthErrorType;
import io.wanted.market.auth.api.support.error.AuthException;
import org.springframework.stereotype.Component;

@Component
public class AuthValidator {
    public void validate(Auth auth) {
        if (!auth.isAccountNonLocked()) {
            throw new AuthException(AuthErrorType.AUTH_LOCKED_ERROR);
        }
    }
}
