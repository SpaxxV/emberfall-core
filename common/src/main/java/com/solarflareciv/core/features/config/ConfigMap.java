package com.solarflareciv.core.features.config;

import com.solarflareciv.core.features.config.elements.ConfigElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigMap {
    private final Map<String, ConfigElement> config;
    public ConfigMap() {
        config = new HashMap<>();
    }
    public ConfigMap set(String key, ConfigElement value) {
        config.put(key, value);
        return this;
    }
    public ConfigElement get(String key) {
        return config.get(key);
    }
    public Set<String> keys() {
        return config.keySet();
    }
    public boolean contains(String key) {
        return config.containsKey(key);
    }
}
