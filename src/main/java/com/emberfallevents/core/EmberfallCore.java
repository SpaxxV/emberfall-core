package com.emberfallevents.core;

import com.emberfallevents.core.features.DeathBan;
import com.emberfallevents.core.features.GlobalWorldData;
import com.emberfallevents.core.features.MaintenanceMode;
import com.emberfallevents.core.features.StaffNotify;

import com.emberfallevents.core.features.config.ConfigFile;
import com.emberfallevents.core.features.config.ConfigMap;
import com.emberfallevents.core.features.config.elements.ConfigBoolean;
import com.emberfallevents.core.features.config.elements.ConfigLong;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmberfallCore implements ModInitializer {
	public static final String MOD_ID = "emberfall-core";
	public static final Logger LOGGER = LoggerFactory.getLogger("Emberfall-Core");

	@Override
	public void onInitialize() {
		LOGGER.info("Using Emberfall Core v1.2.0");

		ConfigFile efcConfig = ConfigFile.createConfig("emberfall-core", new ConfigMap()
				.set("do_death_bans", new ConfigBoolean(true))
				.set("death_ban_duration", new ConfigLong(31556952000L))
				.set("enable_playerrevive_compat", new ConfigBoolean(false)));

		GlobalWorldData.initialize();

		StaffNotify.initialize();

		MaintenanceMode.initialize();

		if ((boolean) efcConfig.get("do_death_bans").value) {
			DeathBan.initialize();
		}
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
