package com.solarflareciv.core.features.config.elements;

import com.solarflareciv.core.EmberfallCore;
import com.solarflareciv.core.features.config.ConfigHolder;

public abstract class ConfigElement {
    public abstract Class<?> type();
    public abstract ConfigHolder<?> getValue();
    public abstract String toConfigString();
    public static ConfigElement loadFromString(String identifier, String value) {
        try {
            switch (identifier) {
                case "s" -> {
                    return new ConfigString(value);
                }
                case "b" -> {
                    return new ConfigBoolean(value.equals("true"));
                }
                case "i" -> {
                    return new ConfigInt(Integer.parseInt(value));
                }
                case "l" -> {
                    return new ConfigLong(Long.parseLong(value));
                }
                default -> {
                    return null;
                }
            }
        } catch (NumberFormatException e) {
            EmberfallCore.LOGGER.warn(e.toString());
            return null;
        }
    }
}
