package io.wanted.market.auth.domain.token;

import io.wanted.market.auth.api.support.error.AuthErrorType;
import io.wanted.market.auth.api.support.error.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TokenReader {
    private final TokenRepository tokenRepository;

    private final TokenParser tokenParser;

    public Token readVerified(String refreshToken) {
        tokenParser.verify(refreshToken);
        List<Token> tokens = tokenRepository.read(refreshToken);
        if (tokens.isEmpty()) {
            throw new AuthException(AuthErrorType.TOKEN_NOT_FOUND_ERROR);
        }
        return tokens.get(0);
    }
}
