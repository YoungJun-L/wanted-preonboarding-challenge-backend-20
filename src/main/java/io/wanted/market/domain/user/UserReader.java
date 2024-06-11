package io.wanted.market.domain.user;

import io.wanted.market.domain.support.error.CoreException;
import io.wanted.market.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static io.wanted.market.domain.support.error.CoreErrorType.USER_NOT_FOUND_ERROR;
import static io.wanted.market.domain.support.error.CoreErrorType.USER_WRONG_PASSWORD_ERROR;

@RequiredArgsConstructor
@Component
public class UserReader {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User read(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CoreException(USER_NOT_FOUND_ERROR));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CoreException(USER_WRONG_PASSWORD_ERROR);
        }
        return user;
    }

    public User read(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CoreException(USER_NOT_FOUND_ERROR));
    }
}
