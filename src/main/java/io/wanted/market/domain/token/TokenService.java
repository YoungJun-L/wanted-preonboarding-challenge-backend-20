package io.wanted.market.domain.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenWriter tokenWriter;

    private final TokenGenerator tokenGenerator;

    public TokenPair issue(String userId) {
        TokenPair tokenPair = tokenGenerator.generate(userId);
        tokenWriter.write(tokenPair);
        return tokenPair;
    }
}
