package io.wanted.market.api.controller.v1.request;

import jakarta.validation.constraints.NotBlank;

public record ReissueRequestDto(
        @NotBlank
        String refreshToken
) {
}
