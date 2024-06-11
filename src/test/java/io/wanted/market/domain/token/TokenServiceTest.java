package io.wanted.market.domain.token;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.wanted.market.ContextTest;
import io.wanted.market.domain.support.error.CoreErrorType;
import io.wanted.market.domain.support.error.CoreException;
import io.wanted.market.domain.support.time.TimeHolder;
import io.wanted.market.domain.user.User;
import io.wanted.market.repository.token.TokenRepository;
import io.wanted.market.repository.user.UserRepository;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenServiceTest extends ContextTest {
    private static final String USER_ID = "1234";

    private final TokenService tokenService;

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    private final TimeHolder timeHolder;

    private final String secretKey;

    private final JwtParser jwtParser;

    private final String refreshToken;

    TokenServiceTest(TokenService tokenService,
                     TokenRepository tokenRepository,
                     UserRepository userRepository,
                     TimeHolder timeHolder,
                     @Value("${spring.security.jwt.secret-key}") String secretKey
    ) {
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.timeHolder = timeHolder;
        this.secretKey = secretKey;
        this.jwtParser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build();
        this.refreshToken = Jwts
                .builder()
                .claims(Collections.emptyMap())
                .subject(USER_ID)
                .issuedAt(new Date(timeHolder.now()))
                .expiration(new Date(timeHolder.now() + Duration.ofDays(30L).toMillis()))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    @AfterEach
    void tearDown() {
        tokenRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("토큰 발급 성공")
    @Test
    void issue() {
        // given & when
        TokenPair tokenPair = tokenService.issue(USER_ID);

        // then
        assertThat(tokenPair.userId()).isEqualTo(USER_ID);
        assertDoesNotThrow(() -> jwtParser.parse(tokenPair.accessToken()));
        assertDoesNotThrow(() -> jwtParser.parse(tokenPair.refreshToken()));
    }

    @DisplayName("토큰 발급 시 refresh token 이 저장된다.")
    @Test
    void issueShouldSaveRefreshToken() {
        // given & when
        TokenPair tokenPair = tokenService.issue(USER_ID);

        // then
        List<Token> tokens = tokenRepository.findByUserId(USER_ID);
        assertThat(tokens).hasSize(1);
        assertThat(tokens.get(0).getRefreshToken()).isEqualTo(tokenPair.refreshToken());
    }

    @DisplayName("토큰 발급 시 access token 은 30분간 유효하다.")
    @Test
    void issueAccessTokenValidFor30Minutes() {
        // given & when
        TokenPair tokenPair = tokenService.issue(USER_ID);

        // then
        Long actual = jwtParser.parseSignedClaims(tokenPair.accessToken()).getPayload().getExpiration().getTime();
        Long expected = timeHolder.now() + Duration.ofMinutes(30L).toMillis();
        assertThat(actual).isCloseTo(expected, Offset.offset(1_000L));
    }

    @DisplayName("토큰 발급 시 refresh token 은 30일간 유효하다.")
    @Test
    void issueRefreshTokenValidFor30Days() {
        // given & when
        TokenPair tokenPair = tokenService.issue(USER_ID);

        // then
        Long actual = jwtParser.parseSignedClaims(tokenPair.refreshToken()).getPayload().getExpiration().getTime();
        Long expected = timeHolder.now() + Duration.ofDays(30L).toMillis();
        assertThat(actual).isCloseTo(expected, Offset.offset(1_000L));
    }

    @DisplayName("토큰 발급 시 이전 토큰은 제거된다.")
    @Test
    void issueShouldRemoveOldToken() {
        // given
        Token token = new Token(USER_ID, refreshToken);
        tokenRepository.save(token);

        // when
        tokenService.issue(USER_ID);

        // then
        List<Token> tokens = tokenRepository.findByUserId(USER_ID);
        assertThat(tokens).hasSize(1);
        assertThat(tokens.get(0).getId()).isNotEqualTo(token.getId());
    }

    @DisplayName("토큰 재발급 성공")
    @Test
    void reissue() {
        // given
        User user = new User("username", "password");
        userRepository.save(user);
        String token = Jwts.builder()
                .claims(Collections.emptyMap())
                .subject(String.valueOf(user.getId()))
                .issuedAt(new Date(timeHolder.now()))
                .expiration(new Date(timeHolder.now() + Duration.ofDays(30L).toMillis()))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        // when
        TokenPair tokenPair = tokenService.reissue(token);

        // then
        assertThat(tokenPair.userId()).isEqualTo(String.valueOf(user.getId()));
        assertDoesNotThrow(() -> jwtParser.parse(tokenPair.accessToken()));
    }

    @DisplayName("토큰 재발급 시 refresh token 이 만료되면 실패한다.")
    @Test
    void reissueWithExpiredRefreshToken() {
        // given
        User user = new User("username", "password");
        userRepository.save(user);
        String token = Jwts.builder()
                .claims(Collections.emptyMap())
                .subject(String.valueOf(user.getId()))
                .issuedAt(new Date(timeHolder.now()))
                .expiration(new Date(timeHolder.now()))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        // when & then
        CoreException ex = assertThrows(CoreException.class, () -> tokenService.reissue(token));
        assertThat(ex.getCoreErrorType()).isEqualTo(CoreErrorType.TOKEN_EXPIRED_ERROR);
    }

    @DisplayName("토큰 재발급 시 가입하지 않은 회원이면 실패한다.")
    @Test
    void reissueWithNotRegisteredUser() {
        // given & when & then
        CoreException ex = assertThrows(CoreException.class, () -> tokenService.reissue(refreshToken));
        assertThat(ex.getCoreErrorType()).isEqualTo(CoreErrorType.USER_NOT_FOUND_ERROR);
    }

    @DisplayName("토큰 재발급 시 refresh token 이 7일 내 만료이면 갱신하여 발급한다.")
    @Test
    void reissueShouldIssueBothTokensWhenRefreshTokenExpiresIn7Days() {
        // given
        User user = new User("username", "password");
        userRepository.save(user);
        String token = Jwts.builder()
                .claims(Collections.emptyMap())
                .subject(String.valueOf(user.getId()))
                .issuedAt(new Date(timeHolder.now()))
                .expiration(new Date(timeHolder.now() + Duration.ofDays(7L).toMillis()))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        // when
        TokenPair tokenPair = tokenService.reissue(token);

        // then
        assertDoesNotThrow(() -> jwtParser.parse(tokenPair.accessToken()));
        assertThat(tokenPair.accessTokenExpiresIn()).isPositive();
        assertDoesNotThrow(() -> jwtParser.parse(tokenPair.refreshToken()));
        assertThat(tokenPair.refreshTokenExpiresIn()).isPositive();
    }

    @DisplayName("토큰 재발급 시 refresh token 만료가 7일 이상 남았으면 access token 만 발급한다.")
    @Test
    void reissueShouldIssueOnlyAccessTokenWhenExpiresMoreThan7Days() {
        // given
        User user = new User("username", "password");
        userRepository.save(user);
        String token = Jwts.builder()
                .claims(Collections.emptyMap())
                .subject(String.valueOf(user.getId()))
                .issuedAt(new Date(timeHolder.now()))
                .expiration(new Date(timeHolder.now() + Duration.ofDays(8L).toMillis()))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        // when
        TokenPair tokenPair = tokenService.reissue(token);

        // then
        assertDoesNotThrow(() -> jwtParser.parse(tokenPair.accessToken()));
        assertThat(tokenPair.accessTokenExpiresIn()).isPositive();
        assertThat(tokenPair.refreshToken()).isNull();
        assertThat(tokenPair.refreshTokenExpiresIn()).isNull();
    }
}
