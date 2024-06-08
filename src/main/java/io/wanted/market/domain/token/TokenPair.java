package io.wanted.market.domain.token;

public record TokenPair(
        String userId,
        String accessToken,
        Long accessTokenExpiresIn,
        String refreshToken,
        Long refreshTokenExpiresIn
) {
    public Token toEntity() {
        return new Token(userId, refreshToken);
    }
}