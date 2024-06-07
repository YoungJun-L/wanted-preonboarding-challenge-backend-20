package io.wanted.market.domain.support;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class CoreTimeHolder implements TimeHolder {
    @Override
    public Long now() {
        return System.currentTimeMillis();
    }
}
