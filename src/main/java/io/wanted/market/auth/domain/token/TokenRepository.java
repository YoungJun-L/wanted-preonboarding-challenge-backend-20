package io.wanted.market.auth.domain.token;

import io.wanted.market.auth.domain.auth.Auth;

public interface TokenRepository {
    Token save(Auth auth, TokenPair tokenPair);
}
