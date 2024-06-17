package io.wanted.market.auth.storage.token;

import io.wanted.market.auth.domain.token.Token;
import io.wanted.market.auth.domain.token.TokenPair;
import io.wanted.market.auth.domain.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TokenCoreRepository implements TokenRepository {
    private final TokenJpaRepository tokenJpaRepository;

    @Transactional
    public Token save(TokenPair tokenPair) {
        Long authId = tokenPair.authId();
        if (tokenJpaRepository.existsByAuthId(authId)) {
            tokenJpaRepository.deleteByAuthId(authId);
        }
        TokenEntity tokenEntity = new TokenEntity(authId, tokenPair.refreshToken());
        tokenJpaRepository.save(tokenEntity);
        return Token.from(tokenEntity);
    }

    @Override
    public List<Token> find(String refreshToken) {
        return tokenJpaRepository.findByRefreshToken(refreshToken).stream()
                .map(Token::from)
                .toList();
    }
}