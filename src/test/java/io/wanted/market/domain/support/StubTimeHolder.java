package io.wanted.market.domain.support;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class StubTimeHolder implements TimeHolder {
    @Override
    public Long now() {
        return 0L;
    }
}
