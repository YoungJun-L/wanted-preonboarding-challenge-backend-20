package io.wanted.market.domain.user;

import io.wanted.market.domain.error.CoreException;
import io.wanted.market.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static io.wanted.market.domain.error.CoreErrorType.USER_NOT_FOUND_ERROR;
import static io.wanted.market.domain.error.CoreErrorType.USER_WRONG_PASSWORD_ERROR;

@RequiredArgsConstructor
@Component
public class UserReader {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User read(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CoreException(USER_NOT_FOUND_ERROR));

        String encodedPassword = passwordEncoder.encode(password);
        if (user.isNotMatchedPassword(encodedPassword)) {
            throw new CoreException(USER_WRONG_PASSWORD_ERROR);
        }
        return user;
    }
}
