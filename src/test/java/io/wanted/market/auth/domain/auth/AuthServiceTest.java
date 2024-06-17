package io.wanted.market.auth.domain.auth;

import io.wanted.market.ContextTest;
import io.wanted.market.auth.api.support.error.AuthErrorType;
import io.wanted.market.auth.api.support.error.AuthException;
import io.wanted.market.auth.storage.auth.AuthJpaRepository;
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

    private final PasswordEncoder passwordEncoder;

    AuthServiceTest(
            AuthService authService,
            AuthRepository authRepository,
            AuthJpaRepository authJpaRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authService = authService;
        this.authRepository = authRepository;
        this.authJpaRepository = authJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @AfterEach
    void tearDown() {
        authJpaRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입 성공")
    @Test
    void register() {
        // given
        NewAuth newAuth = new NewAuth(VALID_USERNAME, VALID_PASSWORD, AuthStatus.ENABLED);

        // when
        authService.register(newAuth);

        // then
        List<Auth> actual = authRepository.read(VALID_USERNAME);
        assertThat(actual).isNotEmpty();
        assertThat(actual.get(0).getUsername()).isEqualTo(VALID_USERNAME);
    }

    @DisplayName("회원가입 시 아이디가 중복되면 실패한다.")
    @Test
    void registerWithDuplicateUsername() {
        // given
        NewAuth newAuth = new NewAuth(VALID_USERNAME, VALID_PASSWORD, AuthStatus.ENABLED);
        authRepository.write(newAuth);

        // when & then
        AuthException ex = assertThrows(AuthException.class, () -> authService.register(newAuth));
        assertThat(ex.getAuthErrorType()).isEqualTo(AuthErrorType.AUTH_DUPLICATE_ERROR);
    }

    @DisplayName("회원가입 시 비밀번호는 인코딩된다.")
    @Test
    void registerShouldEncodePassword() {
        // given
        NewAuth newAuth = new NewAuth(VALID_USERNAME, VALID_PASSWORD, AuthStatus.ENABLED);

        // when
        authService.register(newAuth);

        // then
        List<Auth> actual = authRepository.read(VALID_USERNAME);
        assertThat(actual).isNotEmpty();
        assertThat(passwordEncoder.matches(VALID_PASSWORD, actual.get(0).getPassword())).isTrue();
    }
}
