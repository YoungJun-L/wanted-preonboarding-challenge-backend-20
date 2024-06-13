package io.wanted.market.auth.domain.auth;

import io.wanted.market.auth.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthWriter {
    private final AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder;

    public void write(User user, Auth auth) {
        String encodedPassword = passwordEncoder.encode(auth.password());
        authRepository.save(user, Auth.enabled(auth.username(), encodedPassword));
    }
}
