package io.wanted.market.auth.api.controller.v1;

import io.wanted.market.auth.api.controller.v1.request.ReissueRequestDto;
import io.wanted.market.auth.api.controller.v1.response.TokenResponseDto;
import io.wanted.market.auth.domain.token.TokenPair;
import io.wanted.market.auth.domain.token.TokenService;
import io.wanted.market.core.api.support.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/auth/reissue")
    public ApiResponse<TokenResponseDto> reissue(@RequestBody @Valid ReissueRequestDto reissueRequestDto) {
        TokenPair tokenPair = tokenService.reissue(reissueRequestDto.refreshToken());
        return ApiResponse.success(TokenResponseDto.from(tokenPair));
    }
}
