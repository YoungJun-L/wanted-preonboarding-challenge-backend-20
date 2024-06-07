package io.wanted.market.domain.user;

import io.wanted.market.domain.error.CoreErrorType;
import io.wanted.market.domain.error.CoreException;
import io.wanted.market.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserProcessor {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Long add(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new CoreException(CoreErrorType.USER_DUPLICATE_ERROR);
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword);
        userRepository.save(user);
        return user.getId();
    }
}
