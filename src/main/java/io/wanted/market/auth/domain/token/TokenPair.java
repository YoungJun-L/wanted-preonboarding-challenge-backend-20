package io.wanted.market.auth.domain.token;

public record TokenPair(
        Long authId,
        String accessToken,
        Long accessTokenExpiresIn,
        String refreshToken,
        Long refreshTokenExpiresIn
) {
}
