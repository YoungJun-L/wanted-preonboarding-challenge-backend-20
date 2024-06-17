package io.wanted.market.auth.api.controller.v1.response;

import io.wanted.market.auth.domain.auth.Auth;

public record RegisterResponseDto(
        Long id
) {
    public static RegisterResponseDto from(Auth auth) {
        return new RegisterResponseDto(auth.id());
    }
}
