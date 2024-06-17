package io.wanted.market.auth.domain.token;

import java.util.List;

public interface TokenRepository {
    Token save(TokenPair tokenPair);

    List<Token> find(String refreshToken);
}
