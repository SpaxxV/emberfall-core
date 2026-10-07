package com.solarflareciv.core.features.config.elements;

import com.solarflareciv.core.features.config.ConfigHolder;

public class ConfigBoolean extends ConfigElement {
    private final boolean value;
    public ConfigBoolean(boolean b) {
        value = b;
    }
    @Override
    public ConfigHolder<Boolean> getValue() {
        return new ConfigHolder<>(value);
    }
    @Override
    public String toConfigString() {
        return "b:" + (value ? "true" : "false");
    }
    @Override
    public Class<Boolean> type() { return Boolean.class; }
}
