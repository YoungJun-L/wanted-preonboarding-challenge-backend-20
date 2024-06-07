package io.wanted.market.api.controller.v1.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequestDto(
        @NotBlank
        @Pattern(regexp = "^(?!.*\\s)(?=.*[a-zA-Z])(?=.*\\d).{8,49}$", message = "Username validation error")
        String username,

        @NotBlank
        @Pattern(regexp = "^(?!.*\\s)(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^*+=-]).{10,49}$", message = "Password validation error")
        String password
) {
}
