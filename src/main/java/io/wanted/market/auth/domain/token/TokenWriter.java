package io.wanted.market.auth.domain.token;

import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.storage.token.TokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class TokenWriter {
    private final TokenJpaRepository tokenJpaRepository;

    @Transactional
    public Long write(Auth auth, TokenPair tokenPair) {
        if (tokenPair.refreshToken() == null) {
            return null;
        }

        if (tokenJpaRepository.existsByAuthId(auth.id())) {
            tokenJpaRepository.deleteByAuthId(auth.id());
        }
        TokenEntity tokenEntity = tokenPair.toEntity(auth.id());
        tokenJpaRepository.save(tokenEntity);
        return tokenEntity.getId();
    }
}
