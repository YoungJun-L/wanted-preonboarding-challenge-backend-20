package io.wanted.market.auth.domain.token;

public record TokenPair(
        String accessToken,
        Long accessTokenExpiresIn,
        String refreshToken,
        Long refreshTokenExpiresIn
) {
    public TokenEntity toEntity(Long authId) {
        return new TokenEntity(authId, refreshToken);
    }
}
