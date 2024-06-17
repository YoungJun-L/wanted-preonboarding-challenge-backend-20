package io.wanted.market.auth.storage.token;

import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.domain.token.Token;
import io.wanted.market.auth.domain.token.TokenPair;
import io.wanted.market.auth.domain.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class TokenCoreRepository implements TokenRepository {
    private final TokenJpaRepository tokenJpaRepository;

    @Transactional
    public Token save(Auth auth, TokenPair tokenPair) {
        if (tokenJpaRepository.existsByAuthId(auth.id())) {
            tokenJpaRepository.deleteByAuthId(auth.id());
        }
        TokenEntity tokenEntity = new TokenEntity(auth.id(), tokenPair.refreshToken());
        tokenJpaRepository.save(tokenEntity);
        return Token.from(auth, tokenEntity);
    }
}
