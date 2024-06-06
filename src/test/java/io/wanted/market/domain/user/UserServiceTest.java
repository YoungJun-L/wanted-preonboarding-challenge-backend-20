package io.wanted.market.domain.user;

import io.wanted.market.ContextTest;
import io.wanted.market.domain.error.CoreErrorType;
import io.wanted.market.domain.error.CoreException;
import io.wanted.market.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest extends ContextTest {
    private final UserService userService;

    private final UserRepository userRepository;

    UserServiceTest(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("가입 성공")
    @Test
    void register() {
        // given
        String username = "username";
        String password = "password";

        // when
        userService.register(username, password);

        // then
        Optional<User> actual = userRepository.findByUsername(username);
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getUsername()).isEqualTo(username);
    }

    @DisplayName("가입 시 아이디가 중복되면 실패한다.")
    @Test
    void registerWithDuplicateUsername() {
        // given
        User user = new User("username", "password");
        userRepository.save(user);

        // when & then
        CoreException ex = assertThrows(CoreException.class, () -> userService.register("username", "password"));
        assertThat(ex.getCoreErrorType()).isEqualTo(CoreErrorType.USER_DUPLICATE_ERROR);
    }
}
