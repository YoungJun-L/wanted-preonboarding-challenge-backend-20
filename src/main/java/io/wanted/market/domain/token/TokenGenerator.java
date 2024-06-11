package io.wanted.market.domain.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.wanted.market.domain.support.time.TimeHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenGenerator {
    private final String secretKey;

    private final Long accessExp;

    private final Long refreshExp;

    private final TimeHolder timeHolder;

    public TokenGenerator(
            @Value("${spring.security.jwt.secret-key}") String secretKey,
            @Value("${spring.security.jwt.exp.access}") Long accessExp,
            @Value("${spring.security.jwt.exp.refresh}") Long refreshExp,
            TimeHolder timeHolder
    ) {
        this.secretKey = secretKey;
        this.accessExp = accessExp;
        this.refreshExp = refreshExp;
        this.timeHolder = timeHolder;
    }

    public TokenPair generate(String subject) {
        Long now = timeHolder.now();
        String accessToken = generateAccessToken(subject, now);
        String refreshToken = generateRefreshToken(subject, now);
        return new TokenPair(
                subject,
                accessToken,
                now + accessExp,
                refreshToken,
                now + refreshExp
        );
    }

    private String generateAccessToken(String subject, Long issuedAt) {
        return buildToken(new HashMap<>(), subject, issuedAt, accessExp);
    }

    private String generateRefreshToken(String subject, Long issuedAt) {
        return buildToken(new HashMap<>(), subject, issuedAt, refreshExp);
    }

    private String buildToken(Map<String, Object> extraClaims, String subject, Long issuedAt, Long expiration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(new Date(issuedAt))
                .expiration(new Date(issuedAt + expiration))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
