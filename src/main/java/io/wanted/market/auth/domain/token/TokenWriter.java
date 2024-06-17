package io.wanted.market.auth.domain.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenWriter {
    private final TokenRepository tokenRepository;

    public Token write(TokenPair tokenPair) {
        return tokenRepository.save(tokenPair);
    }
}
