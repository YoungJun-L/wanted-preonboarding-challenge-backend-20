package io.wanted.market.auth.domain.token;

import java.util.List;

public interface TokenRepository {
    Token write(TokenPair tokenPair);

    List<Token> read(String refreshToken);
}
