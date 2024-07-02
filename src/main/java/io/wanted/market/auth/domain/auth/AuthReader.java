package io.wanted.market.auth.domain.auth;

import io.wanted.market.auth.api.support.error.AuthErrorType;
import io.wanted.market.auth.api.support.error.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthReader {
    private final AuthRepository authRepository;

    public Auth read(String username) {
        return authRepository.read(username)
                .orElseThrow(() -> new UsernameNotFoundException(AuthErrorType.AUTH_NOT_FOUND_ERROR.getMessage()));
    }

    public Auth readEnabled(Long id) {
        Auth auth = authRepository.read(id)
                .orElseThrow(() -> new AuthException(AuthErrorType.UNAUTHORIZED_ERROR));
        auth.verify();
        return auth;
    }
}
