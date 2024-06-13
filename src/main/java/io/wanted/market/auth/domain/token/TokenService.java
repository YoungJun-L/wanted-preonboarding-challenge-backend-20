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

    public TokenPair issue(String userId) {
        TokenPair tokenPair = tokenGenerator.issue(userId);
        tokenWriter.write(tokenPair);
        return tokenPair;
    }

    public TokenPair reissue(String refreshToken) {
        String authId = tokenParser.parseSubject(refreshToken);
        Auth auth = authReader.read(Long.valueOf(authId));
        authValidator.validate(auth);
        TokenPair tokenPair = tokenGenerator.reissue(authId, refreshToken);
        tokenWriter.write(tokenPair);
        return tokenPair;
    }
}
