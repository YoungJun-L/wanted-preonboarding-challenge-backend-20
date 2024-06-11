package io.wanted.market.domain.user;

import io.wanted.market.ContextTest;
import io.wanted.market.domain.support.error.CoreErrorType;
import io.wanted.market.domain.support.error.CoreException;
import io.wanted.market.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest extends ContextTest {
    private static final String VALID_USERNAME = "username123";

    private static final String VALID_PASSWORD = "password123!";

    private final UserService userService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    UserServiceTest(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입 성공")
    @Test
    void register() {
        // given & when
        userService.register(VALID_USERNAME, VALID_PASSWORD);

        // then
        Optional<User> actual = userRepository.findByUsername(VALID_USERNAME);
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getUsername()).isEqualTo(VALID_USERNAME);
    }

    @DisplayName("회원가입 시 아이디가 중복되면 실패한다.")
    @Test
    void registerWithDuplicateUsername() {
        // given
        User user = new User(VALID_USERNAME, VALID_PASSWORD);
        userRepository.save(user);

        // when & then
        CoreException ex = assertThrows(CoreException.class, () -> userService.register(VALID_USERNAME, VALID_PASSWORD));
        assertThat(ex.getCoreErrorType()).isEqualTo(CoreErrorType.USER_DUPLICATE_ERROR);
    }

    @DisplayName("회원가입 시 비밀번호는 인코딩된다.")
    @Test
    void registerShouldEncodePassword() {
        // given & when
        userService.register(VALID_USERNAME, VALID_PASSWORD);

        // then
        Optional<User> optionalUser = userRepository.findByUsername(VALID_USERNAME);
        assertThat(optionalUser).isNotEmpty();
        assertThat(passwordEncoder.matches(VALID_PASSWORD, optionalUser.get().getPassword())).isTrue();
    }

    @DisplayName("회원 검증 성공")
    @Test
    void validate() {
        // given
        String encodedPassword = passwordEncoder.encode(VALID_PASSWORD);
        User user = new User(VALID_USERNAME, encodedPassword);
        userRepository.save(user);

        // when & then
        assertDoesNotThrow(() -> userService.validate(VALID_USERNAME, VALID_PASSWORD));
    }

    @DisplayName("가입하지 않은 회원을 검증 시 실패한다.")
    @Test
    void validateNotRegistered() {
        // given & when & then
        CoreException ex = assertThrows(CoreException.class, () -> userService.validate(VALID_USERNAME, VALID_PASSWORD));
        assertThat(ex.getCoreErrorType()).isEqualTo(CoreErrorType.USER_NOT_FOUND_ERROR);
    }

    @DisplayName("비밀번호가 일치하지 않으면 검증 시 실패한다.")
    @Test
    void validateNotMatchedPassword() {
        // given
        User user = new User(VALID_USERNAME, VALID_PASSWORD);
        userRepository.save(user);

        // when & then
        CoreException ex = assertThrows(CoreException.class, () -> userService.validate(VALID_USERNAME, "wrongPassword"));
        assertThat(ex.getCoreErrorType()).isEqualTo(CoreErrorType.USER_WRONG_PASSWORD_ERROR);
    }
}
