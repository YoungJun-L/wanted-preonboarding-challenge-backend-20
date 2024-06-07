package io.wanted.market.domain.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProcessor tokenProcessor;

    private final TokenGenerator tokenGenerator;

    public TokenPair issue(String userId) {
        TokenPair tokenPair = tokenGenerator.generate(userId);
        tokenProcessor.add(tokenPair);
        return tokenPair;
    }
}
