package io.wanted.market.domain.token;

import io.wanted.market.repository.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class TokenProcessor {
    private final TokenRepository tokenRepository;

    @Transactional
    public Long add(TokenPair tokenPair) {
        if (tokenRepository.existsByUserId(tokenPair.userId())) {
            tokenRepository.deleteByUserId(tokenPair.userId());
        }
        Token token = tokenPair.toEntity();
        tokenRepository.save(token);
        return token.getId();
    }
}
