package com.solarflareciv.core;

import com.solarflareciv.core.features.config.ConfigFile;
import com.solarflareciv.core.features.config.ConfigMap;
import com.solarflareciv.core.features.config.elements.ConfigBoolean;
import com.solarflareciv.core.features.config.elements.ConfigLong;
import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class EmberfallCore {
    public static final String MOD_ID = "emberfall-core";
    public static final Logger LOGGER = LoggerFactory.getLogger("Emberfall-Core");
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
    public static Path configLocation;
    public static ConfigFile efcConfig;
    public static EmberfallCoreInitializer initializer;
    public static void onInitialize(EmberfallCoreInitializer init) {
        initializer = init;
        ConfigMap defaultConfig = new ConfigMap()
                .set("enable_death_bans", new ConfigBoolean(true))
                .set("enable_staff_notify", new ConfigBoolean(true))
                .set("enable_maintenance_mode", new ConfigBoolean(true))
                .set("death_ban_duration", new ConfigLong(31556952000L));
        initializer.createConfigs(defaultConfig);
        if ((boolean) EmberfallCore.efcConfig.get("enable_death_bans").value) initializer.initializeDeathBan();
        if ((boolean) EmberfallCore.efcConfig.get("enable_staff_notify").value) initializer.initializeStaffNotify();
        if ((boolean) EmberfallCore.efcConfig.get("enable_maintenance_mode").value) initializer.initializeMaintenanceMode();
    }
}