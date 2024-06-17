package io.wanted.market.auth.domain.token;

import io.wanted.market.auth.domain.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenWriter {
    private final TokenRepository tokenRepository;

    public Token write(Auth auth, TokenPair tokenPair) {
        return tokenRepository.save(auth, tokenPair);
    }
}
