package io.wanted.market.auth.domain.auth;

import io.wanted.market.auth.api.support.error.AuthApiException;
import io.wanted.market.auth.api.support.error.AuthErrorType;
import org.springframework.stereotype.Component;

@Component
public class AuthValidator {
    public void validate(Auth auth) {
        if (!auth.isAccountNonLocked()) {
            throw new AuthApiException(AuthErrorType.AUTH_LOCKED_ERROR);
        }
    }
}
