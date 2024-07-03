package io.wanted.market.auth.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {
    private final AuthReader authReader;

    private final AuthWriter authWriter;

    @Override
    public Auth loadUserByUsername(String username) {
        return authReader.read(username);
    }

    public Auth register(NewAuth newAuth) {
        return authWriter.write(newAuth);
    }
}
