package io.wanted.market.auth.api.controller.v1;

import io.wanted.market.auth.api.controller.v1.request.RegisterAuthRequestDto;
import io.wanted.market.auth.api.controller.v1.response.RegisterAuthResponseDto;
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
    public ApiResponse<RegisterAuthResponseDto> register(@RequestBody @Valid RegisterAuthRequestDto request) {
        Auth auth = authService.register(request.toNewAuth());
        return ApiResponse.success(RegisterAuthResponseDto.from(auth));
    }
}
