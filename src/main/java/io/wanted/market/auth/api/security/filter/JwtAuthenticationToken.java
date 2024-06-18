package io.wanted.market.auth.api.security.filter;

import io.wanted.market.auth.domain.auth.Auth;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = false)
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final Long userId;

    private final Map<String, String> details;

    private JwtAuthenticationToken(
            Long userId,
            Map<String, String> details,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
        this.userId = userId;
        this.details = details;
        super.setAuthenticated(true);
    }

    public static JwtAuthenticationToken authenticated(Auth auth) {
        return new JwtAuthenticationToken(auth.id(), auth.details(), auth.getAuthorities());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }
}
