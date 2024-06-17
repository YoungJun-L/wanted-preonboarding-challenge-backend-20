package io.wanted.market.auth.api.controller.v1;

import io.wanted.market.auth.api.controller.v1.request.RegisterRequestDto;
import io.wanted.market.auth.api.controller.v1.response.RegisterResponseDto;
import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.domain.auth.AuthService;
import io.wanted.market.core.api.support.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/register")
    public ApiResponse<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto registerRequestDto) {
        Auth auth = authService.register(registerRequestDto.toNewAuth());
        return ApiResponse.success(RegisterResponseDto.from(auth));
    }
}
