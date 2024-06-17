package io.wanted.market.auth.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthService implements UserDetailsService {
    private final AuthReader authReader;

    private final AuthWriter authWriter;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return authReader.read(username);
    }

    public void register(Auth auth) {
        authReader.validateDuplicate(auth.username());
        authWriter.write(auth);
    }
}
