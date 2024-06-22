package io.wanted.market.auth.api.controller.v1.response;

import io.wanted.market.auth.domain.auth.Auth;

public record RegisterAuthResponseDto(
        Long userId
) {
    public static RegisterAuthResponseDto from(Auth auth) {
        return new RegisterAuthResponseDto(auth.id());
    }
}
