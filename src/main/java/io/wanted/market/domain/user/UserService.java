package io.wanted.market.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserReader userReader;

    private final UserValidator userValidator;

    private final UserProcessor userProcessor;

    public Long validate(String username, String password) {
        User user = userReader.read(username, password);
        userValidator.validate(user);
        return user.getId();
    }

    public void register(String username, String password) {
        userProcessor.add(username, password);
    }
}
