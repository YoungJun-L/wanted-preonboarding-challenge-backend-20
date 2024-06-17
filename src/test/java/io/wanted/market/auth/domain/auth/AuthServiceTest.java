package io.wanted.market.auth.domain.auth;

import io.wanted.market.ContextTest;
import io.wanted.market.auth.api.support.error.AuthApiException;
import io.wanted.market.auth.api.support.error.AuthErrorType;
import io.wanted.market.auth.storage.auth.AuthJpaRepository;
import io.wanted.market.auth.storage.user.UserJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthServiceTest extends ContextTest {
    private static final String VALID_USERNAME = "username123";

    private static final String VALID_PASSWORD = "password123!";

    private final AuthService authService;

    private final AuthRepository authRepository;

    private final AuthJpaRepository authJpaRepository;

    private final UserJpaRepository userJpaRepository;

    private final PasswordEncoder passwordEncoder;

    AuthServiceTest(
            AuthService authService,
            AuthRepository authRepository,
            AuthJpaRepository authJpaRepository,
            UserJpaRepository userJpaRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authService = authService;
        this.authRepository = authRepository;
        this.authJpaRepository = authJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @AfterEach
    void tearDown() {
        authJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입 성공")
    @Test
    void register() {
        // given
        Auth auth = Auth.enabled(VALID_USERNAME, VALID_PASSWORD);

        // when
        authService.register(auth);

        // then
        List<Auth> actual = authRepository.findByUsername(VALID_USERNAME);
        assertThat(actual).isNotEmpty();
        assertThat(actual.get(0).getUsername()).isEqualTo(VALID_USERNAME);
    }

    @DisplayName("회원가입 시 아이디가 중복되면 실패한다.")
    @Test
    void registerWithDuplicateUsername() {
        // given
        Auth auth = Auth.enabled(VALID_USERNAME, VALID_PASSWORD);
        authRepository.save(auth);

        // when & then
        AuthApiException ex = assertThrows(AuthApiException.class, () -> authService.register(auth));
        assertThat(ex.getAuthErrorType()).isEqualTo(AuthErrorType.AUTH_DUPLICATE_ERROR);
    }

    @DisplayName("회원가입 시 비밀번호는 인코딩된다.")
    @Test
    void registerShouldEncodePassword() {
        // given
        Auth auth = Auth.enabled(VALID_USERNAME, VALID_PASSWORD);

        // when
        authService.register(auth);

        // then
        List<Auth> actual = authRepository.findByUsername(VALID_USERNAME);
        assertThat(actual).isNotEmpty();
        assertThat(passwordEncoder.matches(VALID_PASSWORD, actual.get(0).getPassword())).isTrue();
    }
}
