package io.wanted.market.domain.token;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.wanted.market.ContextTest;
import io.wanted.market.domain.support.TimeHolder;
import io.wanted.market.repository.token.TokenRepository;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TokenServiceTest extends ContextTest {
    private final TokenService tokenService;

    private final TokenRepository tokenRepository;

    private final TimeHolder timeHolder;

    private final JwtParser jwtParser;

    TokenServiceTest(TokenService tokenService,
                     TokenRepository tokenRepository,
                     TimeHolder timeHolder,
                     @Value("${spring.security.jwt.secret-key}") String secretKey
    ) {
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
        this.timeHolder = timeHolder;
        this.jwtParser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build();
    }

    @AfterEach
    void tearDown() {
        tokenRepository.deleteAllInBatch();
    }

    @DisplayName("토큰 발급 성공")
    @Test
    void issue() {
        // given
        String userId = "userId";

        // when
        TokenPair tokenPair = tokenService.issue(userId);

        // then
        assertThat(tokenPair.userId()).isEqualTo(userId);
        assertDoesNotThrow(() -> jwtParser.parse(tokenPair.accessToken()));
        assertDoesNotThrow(() -> jwtParser.parse(tokenPair.refreshToken()));
    }

    @DisplayName("토큰 발급 시 refresh token 이 저장된다.")
    @Test
    void issueShouldSaveRefreshToken() {
        // given
        String userId = "userId";

        // when
        TokenPair tokenPair = tokenService.issue(userId);

        // then
        List<Token> tokens = tokenRepository.findByUserId(userId);
        assertThat(tokens).hasSize(1);
        assertThat(tokens.get(0).getRefreshToken()).isEqualTo(tokenPair.refreshToken());
    }

    @DisplayName("토큰 발급 시 access token 은 30분간 유효하다.")
    @Test
    void issueAccessTokenValidFor30Minutes() {
        // given
        String userId = "userId";

        // when
        TokenPair tokenPair = tokenService.issue(userId);

        // then
        Long actual = jwtParser.parseSignedClaims(tokenPair.accessToken()).getPayload().getExpiration().getTime();
        Long expected = timeHolder.now() + Duration.ofMinutes(30L).toMillis();
        assertThat(actual).isCloseTo(expected, Offset.offset(1_000L));
    }

    @DisplayName("토큰 발급 시 refresh token 은 30일간 유효하다.")
    @Test
    void issueRefreshTokenValidFor30Days() {
        // given
        String userId = "userId";

        // when
        TokenPair tokenPair = tokenService.issue(userId);

        // then
        Long actual = jwtParser.parseSignedClaims(tokenPair.refreshToken()).getPayload().getExpiration().getTime();
        Long expected = timeHolder.now() + Duration.ofDays(30L).toMillis();
        assertThat(actual).isCloseTo(expected, Offset.offset(1_000L));
    }

    @DisplayName("토큰 발급 시 이전 토큰은 제거된다.")
    @Test
    void issueShouldRemoveOldToken() {
        // given
        String userId = "userId";
        Token token = new Token(userId, "refreshToken");
        tokenRepository.save(token);

        // when
        TokenPair tokenPair = tokenService.issue(userId);

        // then
        List<Token> tokens = tokenRepository.findByUserId(userId);
        assertThat(tokens).hasSize(1);
        assertThat(tokens.get(0).getRefreshToken()).isEqualTo(tokenPair.refreshToken());
    }
}
