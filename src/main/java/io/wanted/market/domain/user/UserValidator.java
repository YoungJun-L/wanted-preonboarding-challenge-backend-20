package io.wanted.market.domain.user;

import io.wanted.market.domain.error.CoreErrorType;
import io.wanted.market.domain.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {
    public void validate(User user) {
        if (user.isBanned()) {
            throw new CoreException(CoreErrorType.USER_BANNED_ERROR);
        }
    }
}
