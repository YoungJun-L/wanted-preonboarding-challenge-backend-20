package io.wanted.market.domain.token;

public record TokenPair(
        String userId,
        String accessToken,
        Long accessTokenExpiresIn,
        String refreshToken,
        Long refreshTokenExpiresIn
) {
    public static Token toToken(TokenPair tokenPair) {
        return new Token(tokenPair.userId, tokenPair.refreshToken);
    }
}
