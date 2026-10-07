package com.solarflareciv.core.features.config;

import com.solarflareciv.core.EmberfallCore;
import com.solarflareciv.core.features.config.elements.ConfigElement;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigFile {
    private static final Map<String, ConfigFile> configs = new HashMap<>();
    public static ConfigFile getConfig(String location) {
        if (!configs.containsKey(location)) {
            configs.put(location, new ConfigFile(location));
        }
        return configs.get(location);
    }
    public static ConfigFile createConfig(String location, ConfigMap defaults) {
        if (!configs.containsKey(location)) {
            configs.put(location, new ConfigFile(location, defaults));
        }
        return configs.get(location);
    }

    private final String location;
    private final Path directory;
    private final ConfigMap config;
    public ConfigFile(String loc) {
        location = loc;
        directory = EmberfallCore.configLocation.resolve(loc + ".cfg");
        config = new ConfigMap();
        load();
    }
    public ConfigFile(String loc, ConfigMap defaults) {
        location = loc;
        directory = EmberfallCore.configLocation.resolve(loc + ".cfg");
        config = defaults;
        loadAndSave(defaults);
    }
    public ConfigHolder<?> get(String key) {
        return config.get(key).getValue();
    }
    private boolean load() {
        File configFile = directory.toFile();
        if (configFile.exists() && configFile.canRead()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(configFile));

                // This is poorly done but idc to optimize it lmao
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("//")) continue;
                    int splitIndex = line.indexOf("=");
                    if (splitIndex == -1) continue;
                    String line2 = line.substring(splitIndex + 1);
                    line = line.substring(0, splitIndex);
                    splitIndex = line2.indexOf(":");
                    if (splitIndex == -1) continue;
                    String line3 = line2.substring(splitIndex + 1);
                    line2 = line2.substring(0, splitIndex);
                    config.set(line, ConfigElement.loadFromString(line2, line3));
                }

                reader.close();
                return true;
            } catch (IOException ignored) {}
        }
        return false;
    }
    private boolean validateAgainst(ConfigMap map) {
        boolean flag = false;
        for (String key : map.keys()) {
            if (config.contains(key)) {
                ConfigElement ce1 = config.get(key);
                ConfigElement ce2 = map.get(key);

                if (!ce1.type().equals(ce2.type())) {
                    config.set(key, map.get(key));
                    flag = true;
                }
            } else {
                config.set(key, map.get(key));
                flag = true;
            }
        }
        return flag;
    }
    private void loadAndSave(ConfigMap defaults) {
        boolean success = load();
        EmberfallCore.LOGGER.info(load() ? "Successfully loaded config file " + location + ".cfg, performing validation" : "Loading config file " + location + ".cfg failed, loading defaults");
        boolean flag = !success;
        if (success) {
            flag = validateAgainst(defaults);
            EmberfallCore.LOGGER.info(flag ? "Validation failed for one or more keys, regenerating config file " + location + ".cfg with fixed values" : "Validation for config file " + location + ".cfg passed");
        }
        if (flag) {
            File configFile = directory.toFile();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
                writer.write("// Config managed by EF Core. Do not touch.");
                writer.newLine();
                writer.newLine();
                for (String key : config.keys()) {
                    writer.write(key + "=" + config.get(key).toConfigString());
                    writer.newLine();
                }
                writer.flush();
                writer.close();
                EmberfallCore.LOGGER.info("Saved config file " + location + ".cfg");
            } catch (IOException e) {
                EmberfallCore.LOGGER.warn(e.toString());
            }
        }
    }
}
