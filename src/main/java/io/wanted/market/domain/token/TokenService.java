package io.wanted.market.domain.token;

import io.wanted.market.domain.user.User;
import io.wanted.market.domain.user.UserReader;
import io.wanted.market.domain.user.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenWriter tokenWriter;

    private final TokenGenerator tokenGenerator;

    private final TokenParser tokenParser;

    private final UserReader userReader;

    private final UserValidator userValidator;

    public TokenPair issue(String userId) {
        TokenPair tokenPair = tokenGenerator.generate(userId);
        tokenWriter.write(tokenPair);
        return tokenPair;
    }

    public TokenPair reissue(String refreshToken) {
        String userId = tokenParser.parseSubject(refreshToken);
        User user = userReader.read(Long.getLong(userId));
        userValidator.validate(user);
        TokenPair tokenPair = tokenGenerator.generate(userId);
        tokenWriter.write(tokenPair);
        return tokenPair;
    }
}
