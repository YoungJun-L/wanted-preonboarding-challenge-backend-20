package io.wanted.market.domain.user;

import io.wanted.market.domain.error.CoreErrorType;
import io.wanted.market.domain.error.CoreException;
import io.wanted.market.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserProcessor {
    private final UserRepository userRepository;

    public Long add(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new CoreException(CoreErrorType.USER_DUPLICATE_ERROR);
        }

        User user = new User(username, password);
        userRepository.save(user);
        return user.getId();
    }
}
