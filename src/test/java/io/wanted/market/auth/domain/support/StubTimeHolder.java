package io.wanted.market.auth.domain.support;

import io.wanted.market.auth.domain.support.time.TimeHolder;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class StubTimeHolder implements TimeHolder {
    private final Long now = System.currentTimeMillis();

    @Override
    public Long now() {
        return now;
    }
}
