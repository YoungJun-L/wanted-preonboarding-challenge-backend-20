package io.wanted.market.api.controller.v1.response;

import io.wanted.market.domain.token.TokenPair;

public record LoginResponseDto(
        String userId,
        TokenResponseDto tokens
) {
    public static LoginResponseDto from(TokenPair tokenPair) {
        return new LoginResponseDto(tokenPair.userId(), TokenResponseDto.from(tokenPair));
    }
}
