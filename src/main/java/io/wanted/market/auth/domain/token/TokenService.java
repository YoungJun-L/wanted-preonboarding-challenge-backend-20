package io.wanted.market.auth.domain.token;

import io.wanted.market.auth.domain.auth.Auth;
import io.wanted.market.auth.domain.auth.AuthReader;
import io.wanted.market.auth.domain.auth.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenWriter tokenWriter;

    private final TokenGenerator tokenGenerator;

    private final TokenParser tokenParser;

    private final AuthReader authReader;

    private final AuthValidator authValidator;

    public TokenPair issue(Auth auth) {
        TokenPair tokenPair = tokenGenerator.issue(auth);
        tokenWriter.write(auth, tokenPair);
        return tokenPair;
    }

    public TokenPair reissue(String refreshToken) {
        String authId = tokenParser.parseSubject(refreshToken);
        Auth auth = authReader.read(Long.valueOf(authId));
        authValidator.validate(auth);
        TokenPair tokenPair = tokenGenerator.reissue(auth, refreshToken);
        tokenWriter.write(auth, tokenPair);
        return tokenPair;
    }
}
