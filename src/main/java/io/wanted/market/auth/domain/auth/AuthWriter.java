package io.wanted.market.auth.domain.auth;

import io.wanted.market.auth.api.support.error.AuthErrorType;
import io.wanted.market.auth.api.support.error.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthWriter {
    private final AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder;

    public Auth write(NewAuth newAuth) {
        if (authRepository.existsByUsername(newAuth.username())) {
            throw new AuthException(AuthErrorType.AUTH_DUPLICATE_ERROR);
        }
        String encodedPassword = passwordEncoder.encode(newAuth.password());
        return authRepository.write(newAuth.encoded(encodedPassword));
    }
}
