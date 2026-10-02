package com.emberfallevents.core.features.config.elements;

import com.emberfallevents.core.features.config.ConfigHolder;

public class ConfigLong extends ConfigElement {
    private final long value;
    public ConfigLong(long b) {
        value = b;
    }
    @Override
    public ConfigHolder<Long> getValue() {
        return new ConfigHolder<>(value);
    }
    @Override
    public String toConfigString() {
        return "l:" + value;
    }
    @Override
    public Class<Long> type() { return Long.class; }
}
