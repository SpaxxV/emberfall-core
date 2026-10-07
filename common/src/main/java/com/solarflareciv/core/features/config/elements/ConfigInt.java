package com.solarflareciv.core.features.config.elements;

import com.solarflareciv.core.features.config.ConfigHolder;

public class ConfigInt extends ConfigElement {
    private final int value;
    public ConfigInt(int b) {
        value = b;
    }
    @Override
    public ConfigHolder<Integer> getValue() {
        return new ConfigHolder<>(value);
    }
    @Override
    public String toConfigString() {
        return "i:" + value;
    }
    @Override
    public Class<Integer> type() { return Integer.class; }
}
