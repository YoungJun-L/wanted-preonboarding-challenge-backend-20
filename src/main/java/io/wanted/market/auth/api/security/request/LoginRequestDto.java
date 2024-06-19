package io.wanted.market.auth.api.security.request;

public record LoginRequestDto(
        String username,
        String password
) {
}
