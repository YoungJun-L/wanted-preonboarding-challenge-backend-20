package io.wanted.market.auth.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthWriter {
    private final AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder;

    public void write(Auth auth) {
        String encodedPassword = passwordEncoder.encode(auth.password());
        authRepository.save(Auth.enabled(auth.username(), encodedPassword));
    }
}
