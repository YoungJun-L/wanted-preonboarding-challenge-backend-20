package io.wanted.market.domain.support.time;

import org.springframework.stereotype.Component;

@Component
public class CoreTimeHolder implements TimeHolder {
    @Override
    public Long now() {
        return System.currentTimeMillis();
    }
}
