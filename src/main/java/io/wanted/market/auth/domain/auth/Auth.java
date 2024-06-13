package io.wanted.market.auth.domain.auth;

import io.wanted.market.auth.storage.auth.AuthEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record Auth(String username, String password, AuthStatus status) implements UserDetails {
    public static Auth enabled(String username, String password) {
        return new Auth(username, password, AuthStatus.ENABLED);
    }

    public static Auth from(AuthEntity entity) {
        return Auth.enabled(entity.getUsername(), entity.getPassword());
    }

    public AuthEntity toEntity() {
        return new AuthEntity(username, password);
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
}
