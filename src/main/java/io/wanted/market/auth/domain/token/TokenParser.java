package io.wanted.market.auth.domain.token;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.wanted.market.auth.api.support.error.AuthApiException;
import io.wanted.market.auth.api.support.error.AuthErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenParser {
    private final JwtParser jwtParser;

    public TokenParser(@Value("${spring.security.jwt.secret-key}") String secretKey) {
        this.jwtParser = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build();
    }

    public String parseSubject(String token) {
        try {
            return jwtParser.parseSignedClaims(token).getPayload().getSubject();
        } catch (ExpiredJwtException ex) {
            throw new AuthApiException(AuthErrorType.TOKEN_EXPIRED_ERROR);
        } catch (Exception ex) {
            throw new AuthApiException(AuthErrorType.TOKEN_INVALID_ERROR);
        }
    }

    public Long parseExpiration(String token) {
        try {
            return jwtParser.parseSignedClaims(token).getPayload().getExpiration().getTime();
        } catch (ExpiredJwtException ex) {
            throw new AuthApiException(AuthErrorType.TOKEN_EXPIRED_ERROR);
        } catch (Exception ex) {
            throw new AuthApiException(AuthErrorType.TOKEN_INVALID_ERROR);
        }
    }
}
