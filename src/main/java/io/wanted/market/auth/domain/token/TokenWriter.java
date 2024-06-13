package io.wanted.market.auth.domain.token;

import io.wanted.market.auth.storage.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class TokenWriter {
    private final TokenRepository tokenRepository;

    @Transactional
    public Long write(TokenPair tokenPair) {
        if (tokenPair.refreshToken() == null) {
            return null;
        }

        if (tokenRepository.existsByUserId(tokenPair.userId())) {
            tokenRepository.deleteByUserId(tokenPair.userId());
        }
        Token token = tokenPair.toEntity();
        tokenRepository.save(token);
        return token.getId();
    }
}
