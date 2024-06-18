package io.wanted.market.auth.domain.auth;

import io.wanted.market.auth.api.support.error.AuthErrorType;
import io.wanted.market.auth.api.support.error.AuthException;
import io.wanted.market.auth.storage.auth.AuthEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;

public record Auth(Long id, String username, String password, AuthStatus status) implements UserDetails {
    public static Auth from(AuthEntity entity) {
        return new Auth(entity.getId(), entity.getUsername(), entity.getPassword(), entity.getStatus());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.NO_AUTHORITIES;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !status.equals(AuthStatus.LOCKED);
    }

    @Override
    public boolean isEnabled() {
        return status.equals(AuthStatus.ENABLED);
    }

    public void validate() {
        if (!isAccountNonLocked()) {
            throw new AuthException(AuthErrorType.AUTH_LOCKED_ERROR);
        }
    }

    public Map<String, String> details() {
        return Map.of("username", username);
    }
}
