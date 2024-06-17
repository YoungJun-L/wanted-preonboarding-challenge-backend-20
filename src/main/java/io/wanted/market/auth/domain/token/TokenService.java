package io.wanted.market.auth.domain.token;

import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.domain.auth.AuthReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenPairGenerator tokenPairGenerator;

    private final TokenWriter tokenWriter;

    private final TokenReader tokenReader;

    private final AuthReader authReader;

    public TokenPair issue(Auth auth) {
        TokenPair tokenPair = tokenPairGenerator.issue(auth);
        tokenWriter.write(tokenPair);
        return tokenPair;
    }

    public TokenPair reissue(String refreshToken) {
        Token token = tokenReader.readVerified(refreshToken);
        Auth auth = authReader.findEnabled(token.authId());
        TokenPair tokenPair = tokenPairGenerator.issue(auth);
        tokenWriter.write(tokenPair);
        return tokenPair;
    }
}
