package com.solarflareciv.core.features.config.elements;

import com.solarflareciv.core.features.config.ConfigHolder;

public class ConfigString extends ConfigElement {
    private final String value;
    public ConfigString(String s) {
        value = s;
    }
    @Override
    public ConfigHolder<String> getValue() {
        return new ConfigHolder<>(value);
    }
    @Override
    public String toConfigString() {
        return "s:" + value;
    }
    @Override
    public Class<String> type() { return String.class; }
}
